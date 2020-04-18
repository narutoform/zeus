package cn.lcarus.common.service.impl;

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
import cn.lcarus.admin.param.UserAccountDrawApproveQueryParam;
import cn.lcarus.admin.param.UserAccountDrawApproveSubmitParam;
import cn.lcarus.admin.res.UseAccountDrawApproveVo;
import cn.lcarus.common.entity.User;
import cn.lcarus.common.entity.UserAccountDrawApprove;
import cn.lcarus.common.entity.UserAccountRecord;
import cn.lcarus.common.entity.UserThirdInfo;
import cn.lcarus.common.enums.UserAccountDrawApproveStateEnum;
import cn.lcarus.common.mapper.UserAccountDrawApproveMapper;
import cn.lcarus.common.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户提现审核表 服务实现类
 *
 * @author xingcheng
 * @since 2020-04-05
 */
@Service
@Slf4j
public class BaseUserAccountDrawApproveServiceImpl extends BaseServiceImpl<UserAccountDrawApproveMapper, UserAccountDrawApprove> implements BaseUserAccountDrawApproveService {
    
    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    @Lazy
    private BaseUserThirdInfoService baseUserThirdInfoService;

    @Autowired
    private BaseUserAccountRecordService baseUserAccountRecordService;

    @Autowired
    private BaseWxService baseWxService;

    @Value("${wx.miniapp.appid}")
    private String miniAppId;

    @Value("${wx.mp.appId}")
    private String mpAppId;

    @Override
    public ApiResult<Page<UseAccountDrawApproveVo>> showList(UserAccountDrawApproveQueryParam param) {
        Set<Long> userIdSet = null;
        if (StrUtil.isNotBlank(param.getMobile())) {
            List<User> userList = baseUserService.getUserByMobile(param.getMobile());
            if (CollUtil.isNotEmpty(userList)) {
                userIdSet = userList.stream().map(User::getId).collect(Collectors.toSet());
            }
        }
        IPage<UserAccountDrawApprove> page = lambdaQuery()
                .eq(Objects.nonNull(param.getUserId()), UserAccountDrawApprove::getUserId, param.getUserId())
                .eq(Objects.nonNull(param.getUserAccountDrawApproveId()), UserAccountDrawApprove::getId, param.getUserAccountDrawApproveId())
                .eq(Objects.nonNull(param.getState()), UserAccountDrawApprove::getState, param.getState())
                .ge(Objects.nonNull(param.getCreateTimeStart()), UserAccountDrawApprove::getCreateTime, param.getCreateTimeStart())
                .le(Objects.nonNull(param.getCreateTimeEnd()), UserAccountDrawApprove::getCreateTime, param.getCreateTimeEnd())
                .in(CollUtil.isNotEmpty(userIdSet), UserAccountDrawApprove::getUserId, userIdSet)
                .orderByDesc(UserAccountDrawApprove::getId)
                .page(param);
        List<UserAccountDrawApprove> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return ApiResult.ok();
        }
        List<UseAccountDrawApproveVo> collect = records.stream()
                .map(v -> {
                    Long userId = v.getUserId();
                    UseAccountDrawApproveVo useAccountDrawApproveVo = BeanUtil.toBean(v, UseAccountDrawApproveVo.class);
                    // 查询三方用户信息库
                    UserThirdInfo userThirdInfo = baseUserThirdInfoService.getUserThirdInfo(userId, miniAppId);
                    if (Objects.nonNull(userThirdInfo)) {
                        useAccountDrawApproveVo.setNickName(userThirdInfo.getNickName());
                    }
                    // 查询用户信息
                    User user = baseUserService.getById(userId);
                    if (Objects.nonNull(user)) {
                        useAccountDrawApproveVo.setMobile(user.getMobile());
                    }
                    // 查询账户记录信息
                    UserAccountRecord userAccountRecord = baseUserAccountRecordService.getById(v.getUserAccountRecordId());
                    if (Objects.nonNull(userAccountRecord)) {
                        useAccountDrawApproveVo.setDrawAccountAmount(userAccountRecord.getAmount());
                    }
                    return useAccountDrawApproveVo;
                }).collect(Collectors.toList());
        Page<UseAccountDrawApproveVo> useAccountDrawApproveVoPage = new Page<>();
        useAccountDrawApproveVoPage.setSize(param.getSize());
        useAccountDrawApproveVoPage.setTotal(param.getTotal());
        useAccountDrawApproveVoPage.setRecords(collect);
        return ApiResult.ok(useAccountDrawApproveVoPage);
    }

    @Override
    @DistributeLock(value = "USER_ACCOUNT_DRAW_APPROVE:APPROVE", key = "#param.userAccountDrawApproveId", expire = 1024, timeout = 0, errMsg = "正在提现审核中，请不要频繁点击！")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> approve(UserAccountDrawApproveSubmitParam param) {
        Long userAccountDrawApproveId = param.getUserAccountDrawApproveId();
        Assert.notNull(userAccountDrawApproveId, "userAccountDrawApproveId");
        // 参数校验
        UserAccountDrawApprove userAccountDrawApprove = getById(userAccountDrawApproveId);
        if (Objects.isNull(userAccountDrawApprove)) {
            return ApiResult.fail(ApiCode.CODE_100600);
        }
        if (!Objects.equals(userAccountDrawApprove.getState(), UserAccountDrawApproveStateEnum.WAIT.getCode())) {
            return ApiResult.fail(ApiCode.CODE_100601);
        }
        DateTime now = DateUtil.date();
        Long userId = userAccountDrawApprove.getUserId();
        Long userAccountRecordId = userAccountDrawApprove.getUserAccountRecordId();
        // 查询账户记录信息
        UserAccountRecord userAccountRecord = baseUserAccountRecordService.getById(userAccountDrawApprove.getUserAccountRecordId());
        if (Objects.isNull(userAccountRecord)) {
            return ApiResult.fail(ApiCode.CODE_100602);
        }
        BigDecimal amount = userAccountRecord.getAmount();
        if (Objects.isNull(amount)) {
            return ApiResult.fail(ApiCode.CODE_100602);
        }
        Boolean passFlag = param.getPassFlag();
        if (BooleanUtils.isTrue(passFlag)) {
            // 审核通过
            updateUserAccountDrawApprove(param, userAccountDrawApprove, now, UserAccountDrawApproveStateEnum.PASS.getCode());
            // 发送申请，进行提现
            ApiResult<?> sendRealAmountResult = baseWxService.sendRealAmount(userId, amount, mpAppId, userAccountRecordId);
            if (sendRealAmountResult.isSuccess()) {
                // 提现成功后发送通知
                baseWxService.sendWithDrawSuccessNotice(userId, amount);
            } else {
                if (!Objects.equals(sendRealAmountResult.getCode(), ApiCode.CODE_100302.getCode()) && !Objects.equals(sendRealAmountResult.getCode(), ApiCode.CODE_100303.getCode())) {
                    throw new BusinessException(ApiCode.CODE_100301);
                }
            }
        } else {
            // 审核不通过
            updateUserAccountDrawApprove(param, userAccountDrawApprove, now, UserAccountDrawApproveStateEnum.NOT_PASS.getCode());
        }
        return ApiResult.ok();
    }

    private void updateUserAccountDrawApprove(UserAccountDrawApproveSubmitParam param, UserAccountDrawApprove userAccountDrawApprove, DateTime now, Integer state) {
        UserAccountDrawApprove userAccountDrawApproveUpdate = new UserAccountDrawApprove();
        userAccountDrawApproveUpdate.setId(userAccountDrawApprove.getId());
        userAccountDrawApproveUpdate.setState(state);
        userAccountDrawApproveUpdate.setRemark(param.getRemark());
        userAccountDrawApproveUpdate.setVersion(userAccountDrawApprove.getVersion());
        userAccountDrawApproveUpdate.setApproveTime(now);
        userAccountDrawApproveUpdate.setUpdateTime(now);
        boolean update = updateById(userAccountDrawApproveUpdate);
        Assert.isTrue(update);
    }
}
