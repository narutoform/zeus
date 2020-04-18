package cn.lcarus.common.service.impl;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.exception.BusinessException;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.lcarus.common.entity.User;
import cn.lcarus.common.entity.UserAccount;
import cn.lcarus.common.entity.UserAccountDrawApprove;
import cn.lcarus.common.entity.UserAccountRecord;
import cn.lcarus.common.enums.ConfigEnum;
import cn.lcarus.common.enums.UserAccountDrawApproveStateEnum;
import cn.lcarus.common.enums.UserAccountUseTypeEnum;
import cn.lcarus.common.mapper.UserAccountMapper;
import cn.lcarus.common.service.*;
import cn.lcarus.guess.web.param.LoginUserPageVo;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserWithDrewParam;
import cn.lcarus.guess.web.res.UserAccountInfoRes;
import cn.lcarus.guess.web.res.UserAccountRecordInfoRes;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户账户 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Service
@Slf4j
public class BaseUserAccountServiceImpl extends BaseServiceImpl<UserAccountMapper, UserAccount> implements BaseUserAccountService {

    @Autowired
    @Lazy
    private BaseUserAccountService baseUserAccountService;

    @Autowired
    private BaseUserAccountRecordService baseUserAccountRecordService;

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private BaseWxService baseWxService;

    @Autowired
    private BaseConfigService baseConfigService;

    @Autowired
    private BaseUserAccountDrawApproveService baseUserAccountDrawApproveService;

    @Override
    @Retryable(value = {ApiException.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000L, multiplier = 3))
    @Transactional(rollbackFor = Exception.class)
    public Long changeUserAccountAmount(Long userId, BigDecimal amount, Integer useType) {
        Assert.notNull(userId, "userId");
        Assert.notNull(amount, "amount");
        Assert.notNull(useType, "useType");
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) != 0);
        UserAccount userAccount = baseUserAccountService.getUserAccountNewEst(userId);
        Assert.notNull(userAccount, "userAccount");
        DateTime now = DateUtil.date();
        // 更新
        UserAccount userAccountUpdate = new UserAccount();
        userAccountUpdate.setId(userAccount.getId());
        userAccountUpdate.setVersion(userAccount.getVersion());
        userAccountUpdate.setUpdateTime(now);
        userAccountUpdate.setUserId(userId);
        userAccountUpdate.setAmount(userAccount.getAmount().add(amount));
        // 保存账户变更记录
        boolean update = updateById(userAccountUpdate);
        Assert.isTrue(update);
        return baseUserAccountRecordService.saveUserAccountRecord(userId, amount, useType, now);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public UserAccount getUserAccountNewEst(Long userId) {
        Assert.notNull(userId, "userId");
        return lambdaQuery()
                .eq(UserAccount::getUserId, userId)
                .one();
    }

    @Override
    public UserAccount getUserAccount(Long userId) {
        Assert.notNull(userId, "userId");
        return lambdaQuery()
                .eq(UserAccount::getUserId, userId)
                .one();
    }

    @Override
    public UserAccount getUserAccountAmount(Long userId) {
        Assert.notNull(userId, "userId");
        return lambdaQuery()
                .eq(UserAccount::getUserId, userId)
                .select(UserAccount::getAmount)
                .one();
    }

    @Override
    public ApiResult<UserAccountInfoRes> showInfo(LoginUserVo param) {
        // 检验
        Assert.notNull(param, "param");
        Long userId = param.getUserId();
        Assert.notNull(userId, "userId");
        UserAccount userAccount = baseUserAccountService.getUserAccount(userId);
        if (Objects.isNull(userAccount)) {
            return ApiResult.fail(ApiCode.ERROR_6301);
        }
        UserAccountInfoRes userAccountInfoRes = BeanUtil.toBean(userAccount, UserAccountInfoRes.class);
        // 每次提现的最低限
        userAccountInfoRes.setLimitAmountDown(getLimitAmountDownBigDecimal().divide(BigDecimal.valueOf(CommonConstant.DEFAULT_100), CommonConstant.DEFAULT_2, RoundingMode.HALF_UP));
        return ApiResult.ok(userAccountInfoRes);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributeLock(value = "USER_ACCOUNT:DREW", key = "#param.userId", expire = 1200, timeout = 0, errMsg = "正在提现申请中，请不要频繁点击哦！")
    public ApiResult<?> cashWithdrawal(UserWithDrewParam param) {
        // 检验
        Assert.notNull(param, "param");
        Long userId = param.getUserId();
        String appId = param.getAppId();
        Assert.notNull(userId, "userId");
        UserAccount userAccount = baseUserAccountService.getUserAccount(userId);
        if (Objects.isNull(userAccount)) {
            return ApiResult.fail(ApiCode.ERROR_6301);
        }
        // 检测手机号是否获取
        User user = baseUserService.getUser(userId);
        if (Objects.isNull(user)) {
            return ApiResult.fail(ApiCode.ERROR_6400);
        }
        if (StrUtil.isBlank(user.getMobile())) {
            return ApiResult.fail(ApiCode.ERROR_6304);
        }
        // 检测提现金额是否达到限制
        BigDecimal amountFen = userAccount.getAmount().multiply(BigDecimal.valueOf(CommonConstant.DEFAULT_100));
        if (amountFen.compareTo(getLimitAmountDownBigDecimal()) < 0) {
            return ApiResult.fail(ApiCode.ERROR_6302.getCode(), StrUtil.format(ApiCode.ERROR_6302.getMsg(), getLimitAmountDown() / 100));
        }
        if (amountFen.compareTo(getLimitAmountUpBigDecimal()) > 0) {
            return ApiResult.fail(ApiCode.ERROR_6310.getCode(), StrUtil.format(ApiCode.ERROR_6310.getMsg(), getLimitAmountUp() / 100));
        }
        // 账户金额变动
        Long userAccountRecordId = baseUserAccountService.changeUserAccountAmount(userId, userAccount.getAmount().negate(), UserAccountUseTypeEnum.WITH_DREW.getCode());
        // 是否开启提现审核
        Boolean enableApprove = baseConfigService.getBooleanValue(ConfigEnum.USER_ACCOUNT_DRAW_APPROVE_ENABLE.key);
        if (BooleanUtils.isNotFalse(enableApprove)) {
            // 记录审核表
            saveUserAccountDrawApprove(userId, userAccountRecordId);
        } else {
            // 发送申请，进行提现
            ApiResult<?> sendRealAmountResult = baseWxService.sendRealAmount(userId, userAccount.getAmount(), appId, userAccountRecordId);
            if (sendRealAmountResult.isOk()) {
                // 提现成功后发送通知
                baseWxService.sendWithDrawSuccessNotice(userId, userAccount.getAmount());
            } else {
                if (!Objects.equals(sendRealAmountResult.getCode(), ApiCode.ERROR_6307.getCode()) && !Objects.equals(sendRealAmountResult.getCode(), ApiCode.ERROR_6308.getCode())) {
                    throw new BusinessException(ApiCode.ERROR_6305);
                }
            }
        }
        return ApiResult.ok();
    }

    private void saveUserAccountDrawApprove(Long userId, Long userAccountRecordId) {
        DateTime now = DateUtil.date();
        UserAccountDrawApprove userAccountDrawApproveSave = new UserAccountDrawApprove();
        userAccountDrawApproveSave.setUserId(userId);
        userAccountDrawApproveSave.setUserAccountRecordId(userAccountRecordId);
        userAccountDrawApproveSave.setState(UserAccountDrawApproveStateEnum.WAIT.getCode());
        userAccountDrawApproveSave.setCreateTime(now);
        userAccountDrawApproveSave.setUpdateTime(now);
        boolean save = baseUserAccountDrawApproveService.save(userAccountDrawApproveSave);
        Assert.isTrue(save);
    }

    private BigDecimal getLimitAmountUpBigDecimal() {
        return BigDecimal.valueOf(getLimitAmountUp());
    }

    private Long getLimitAmountUp() {
        return baseConfigService.getLongValue(ConfigEnum.WITH_DRAW_LIMIT_AMOUNT_UP.key);
    }

    private BigDecimal getLimitAmountDownBigDecimal() {
        return BigDecimal.valueOf(getLimitAmountDown());
    }

    private Long getLimitAmountDown() {
        return baseConfigService.getLongValue(ConfigEnum.WITH_DRAW_LIMIT_AMOUNT_DOWN.key);
    }

    @Override
    public ApiResult<Page<UserAccountRecordInfoRes>> showRecord(LoginUserPageVo<UserAccountRecord> param) {
        // 检验
        Assert.notNull(param, "param");
        Long userId = param.getUserId();
        Assert.notNull(userId, "userId");

        LoginUserPageVo<UserAccountRecord> page = baseUserAccountRecordService.lambdaQuery()
                .eq(UserAccountRecord::getUserId, userId)
                .orderByDesc(UserAccountRecord::getId)
                .page(param);
        List<UserAccountRecord> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return ApiResult.ok(new Page<>());
        }
        List<UserAccountRecordInfoRes> collect = records
                .stream()
                .map(v -> {
                    UserAccountRecordInfoRes userAccountRecordInfoRes = BeanUtil.toBean(v, UserAccountRecordInfoRes.class);
                    return userAccountRecordInfoRes;
                })
                .collect(Collectors.toList());
        Page<UserAccountRecordInfoRes> pageRes = new Page<>();
        pageRes.setTotal(page.getTotal());
        pageRes.setRecords(collect);
        return ApiResult.ok(pageRes);
    }

    @Override
    @Retryable(value = {ApiException.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000L, multiplier = 3))
    @Transactional(rollbackFor = Exception.class)
    public Long changeUserAccountAmount(Long userId, BigDecimal amount) {
        Assert.notNull(userId, "userId");
        Assert.notNull(amount, "amount");
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) != 0);
        UserAccount userAccount = baseUserAccountService.getUserAccountNewEst(userId);
        Assert.notNull(userAccount, "userAccount");
        DateTime now = DateUtil.date();
        // 更新
        UserAccount userAccountUpdate = new UserAccount();
        userAccountUpdate.setId(userAccount.getId());
        userAccountUpdate.setVersion(userAccount.getVersion());
        userAccountUpdate.setUpdateTime(now);
        userAccountUpdate.setUserId(userId);
        userAccountUpdate.setAmount(userAccount.getAmount().add(amount));
        // 保存账户变更记录
        boolean update = updateById(userAccountUpdate);
        Assert.isTrue(update);
        return userAccount.getId();
    }
}
