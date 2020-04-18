package cn.lcarus.common.service.impl;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.util.ValidatorUtils;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.lcarus.common.entity.UserShareRecord;
import cn.lcarus.common.enums.ConfigEnum;
import cn.lcarus.common.enums.UserAccountUseTypeEnum;
import cn.lcarus.common.enums.UserShareRecordStateEnum;
import cn.lcarus.common.mapper.UserShareRecordMapper;
import cn.lcarus.common.service.BaseConfigService;
import cn.lcarus.common.service.BaseEcpmService;
import cn.lcarus.common.service.BaseUserAccountService;
import cn.lcarus.common.service.BaseUserShareRecordService;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserShareDrawParam;
import cn.lcarus.guess.web.res.UserShareDrawRes;
import cn.lcarus.guess.web.res.UserShareResultRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 用户分享记录表 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Service
@Slf4j
public class BaseUserShareRecordServiceImpl extends BaseServiceImpl<UserShareRecordMapper, UserShareRecord> implements BaseUserShareRecordService {

    @Autowired
    @Lazy
    private BaseUserShareRecordService baseUserShareRecordService;

    @Autowired
    private BaseUserAccountService baseUserAccountService;

    @Autowired
    private BaseEcpmService baseEcpmService;

    @Autowired
    private BaseConfigService baseConfigService;

    @Value("${lcarus.wx.ad.video.share-adUnit-id}")
    private String shareAdUnitId;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributeLock(value = "USER_SHARE_RECORD:SHARE_RESULT", key = "#param.userId", expire = 360, timeout = 0, errMsg = "正在获取分享结果中，请不要频繁点击哦！")
    @Deprecated
    public ApiResult<UserShareResultRes> shareResult(LoginUserVo param) {
        // 校验
        Assert.notNull(param, "param");
        Long userId = param.getUserId();
        Assert.notNull(userId, "userId");
        String today = DateUtil.today();
        UserShareResultRes userShareResultRes = new UserShareResultRes();
        Integer count = countUserShareRecord(userId, today);
        Integer limitCountDayUp = baseConfigService.getIntegerValue(ConfigEnum.LOTTERY_SHARE_LIMIT_COUNT_DAY_UP.key);
        if (count > limitCountDayUp) {
            userShareResultRes.setShareLimitFlag(true);
            return ApiResult.ok(userShareResultRes);
        }
        // 保存分享记录
        UserShareRecord userShareRecordSave = new UserShareRecord();
        userShareRecordSave.setCreateTime(DateUtil.date());
        userShareRecordSave.setUpdateTime(userShareRecordSave.getCreateTime());
        userShareRecordSave.setShareDay(today);
        userShareRecordSave.setUserId(userId);
        userShareRecordSave.setState(UserShareRecordStateEnum.GET_ABLE.getCode());
        // 分配金额
        BigDecimal shareAmount = calShareRedPackAmount(count + 1);
        if (shareAmount.compareTo(BigDecimal.ZERO) == 0) {
            userShareResultRes.setShareLimitFlag(true);
            return ApiResult.ok(userShareResultRes);
        }
        userShareRecordSave.setShareAmount(shareAmount);
        boolean save = save(userShareRecordSave);
        Assert.isTrue(save);
        userShareResultRes.setUserShareRecordId(userShareRecordSave.getId());
        // 广告地址处理
        userShareResultRes.setAdUnitId(shareAdUnitId);
        return ApiResult.ok(userShareResultRes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributeLock(value = "USER_SHARE_RECORD:DREW", key = "#param.userId + ':' + #param.userShareRecordId", expire = 360, timeout = 0, errMsg = "正在获取分享结果中，请不要频繁点击哦！")
    @Deprecated
    public ApiResult<UserShareDrawRes> draw(UserShareDrawParam param) {
        // 校验
        ValidatorUtils.validateModel(param);
        Long userId = param.getUserId();
        Long userShareRecordId = param.getUserShareRecordId();
        Assert.notNull(userId, "userId");
        // 响应
        UserShareDrawRes userShareDrawRes = new UserShareDrawRes();
        DateTime now = DateUtil.date();
        // 查询分享记录并校验
        UserShareRecord userShareRecord = baseUserShareRecordService.getUserShareRecord(userShareRecordId);
        BeanUtil.copyProperties(userShareRecord, userShareDrawRes);
        if (Objects.isNull(userShareRecord)) {
            return ApiResult.fail(ApiCode.ERROR_6200);
        }
        if (!Objects.equals(userShareRecord.getUserId(), userId)) {
            return ApiResult.fail(ApiCode.ERROR_6201);
        }
        if (!Objects.equals(userShareRecord.getState(), UserShareRecordStateEnum.GET_ABLE.getCode())) {
            return ApiResult.fail(ApiCode.ERROR_6203);
        }
        if (!baseUserShareRecordService.isValid(userShareRecord)) {
            return ApiResult.fail(ApiCode.ERROR_6204);
        }
        // 领取逻辑
        // 用户账户变更
        BigDecimal shareAmount = userShareRecord.getShareAmount();
        Long userAccountAmountRecordId = baseUserAccountService.changeUserAccountAmount(userId, shareAmount, UserAccountUseTypeEnum.RED_PACK.getCode());
        // 保存领取记录
        UserShareRecord userShareRecordUpdate = new UserShareRecord();
        userShareRecordUpdate.setId(userShareRecordId);
        userShareRecordUpdate.setState(UserShareRecordStateEnum.GET.getCode());
        userShareRecordUpdate.setVersion(userShareRecord.getVersion());
        userShareRecordUpdate.setUpdateTime(now);
        userShareRecordUpdate.setUserAccountRecordId(userAccountAmountRecordId);
        boolean update = baseUserShareRecordService.updateById(userShareRecordUpdate);
        Assert.isTrue(update);
        userShareDrawRes.setState(UserShareRecordStateEnum.GET.getCode());
        return ApiResult.ok(userShareDrawRes);
    }

    @Override
    public ApiResult<UserShareResultRes> shareResultCallback(LoginUserVo param) {
        // 校验
        Assert.notNull(param, "param");
        Long userId = param.getUserId();
        Assert.notNull(userId, "userId");
        String today = DateUtil.today();
        UserShareResultRes userShareResultRes = new UserShareResultRes();
        Integer count = countUserShareRecord(userId, today);
        Integer limitCountDayUp = baseConfigService.getIntegerValue(ConfigEnum.LOTTERY_SHARE_LIMIT_COUNT_DAY_UP.key);
        if (count + 1 > limitCountDayUp) {
            userShareResultRes.setShareLimitFlag(true);
            return ApiResult.ok(userShareResultRes);
        }
        // 广告地址处理
        userShareResultRes.setAdUnitId(shareAdUnitId);
        return ApiResult.ok(userShareResultRes);
    }

    @Override
    @DistributeLock(value = "USER_SHARE_RECORD:DREW_CALL", key = "#param.userId", expire = 360, timeout = 0, errMsg = "正在领取分享奖励中，请不要频繁点击哦！")
    public ApiResult<UserShareDrawRes> drawCall(LoginUserVo param) {
        // 校验
        ValidatorUtils.validateModel(param);
        Long userId = param.getUserId();
        Assert.notNull(userId, "userId");
        // 响应
        UserShareDrawRes userShareDrawRes = new UserShareDrawRes();
        DateTime now = DateUtil.date();
        String today = DateUtil.today();
        userShareDrawRes.setShareDay(today);
        Integer count = countUserShareRecord(userId, today);
        Integer limitCountDayUp = baseConfigService.getIntegerValue(ConfigEnum.LOTTERY_SHARE_LIMIT_COUNT_DAY_UP.key);
        if (count + 1 > limitCountDayUp) {
            userShareDrawRes.setShareLimitFlag(true);
            return ApiResult.ok(userShareDrawRes);
        }
        // 分配金额
        BigDecimal shareAmount = calShareRedPackAmount(count + 1);
        if (shareAmount.compareTo(BigDecimal.ZERO) == 0) {
            userShareDrawRes.setShareLimitFlag(true);
            return ApiResult.ok(userShareDrawRes);
        }
        userShareDrawRes.setShareAmount(shareAmount);
        // 用户账户变更
        Long userAccountAmountRecordId = baseUserAccountService.changeUserAccountAmount(userId, shareAmount, UserAccountUseTypeEnum.RED_PACK.getCode());
        // 保存分享记录
        UserShareRecord userShareRecordSave = new UserShareRecord();
        userShareRecordSave.setCreateTime(DateUtil.date());
        userShareRecordSave.setUpdateTime(userShareRecordSave.getCreateTime());
        userShareRecordSave.setShareDay(today);
        userShareRecordSave.setUserId(userId);
        userShareRecordSave.setState(UserShareRecordStateEnum.GET.getCode());
        userShareRecordSave.setShareAmount(shareAmount);
        userShareRecordSave.setUserAccountRecordId(userAccountAmountRecordId);
        boolean save = save(userShareRecordSave);
        Assert.isTrue(save);
        userShareDrawRes.setState(UserShareRecordStateEnum.GET.getCode());
        return ApiResult.ok(userShareDrawRes);
    }

    @Override
    public UserShareRecord getUserShareRecord(Long userShareRecordId) {
        Assert.notNull(userShareRecordId, "userShareRecordId");

        return lambdaQuery()
                .eq(UserShareRecord::getId, userShareRecordId)
                .one();
    }

    @Override
    public Integer countUserShareRecord(Long userId, String shareDay) {
        Assert.notNull(userId, "userId");
        Assert.isTrue(StrUtil.isNotBlank(shareDay), "shareDay is null");

        return lambdaQuery()
                .eq(UserShareRecord::getUserId, userId)
                .eq(UserShareRecord::getShareDay, shareDay)
                .count();
    }

    @Override
    public boolean isValid(UserShareRecord userShareRecord) {
        if (Objects.isNull(userShareRecord)) {
            return true;
        }
        return DateUtil.isIn(DateUtil.date(), userShareRecord.getCreateTime(), DateUtil.offsetDay(userShareRecord.getCreateTime(), CommonConstant.DEFAULT_TWO_DAY));
    }

    /**
     * 计算分享红包金额
     *
     * @param count
     * @return
     */
    private BigDecimal calShareRedPackAmount(Integer count) {
        Assert.notNull(count, "count");
        //保留两位小数，并用都好隔开
        BigDecimal base;
        switch (count) {
            case 1:
                base = new BigDecimal("0.01");
                return base.multiply(BigDecimal.valueOf(RandomUtil.randomInt(3, 7)));
            case 2:
                base = new BigDecimal("0.01");
                return base.multiply(BigDecimal.valueOf(RandomUtil.randomInt(2, 5)));
            case 3:
                base = new BigDecimal("0.01");
                return base.multiply(BigDecimal.valueOf(RandomUtil.randomInt(3, 6)));
            case 4:
                base = new BigDecimal("0.01");
                return base.multiply(BigDecimal.valueOf(RandomUtil.randomInt(1, 4)));
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return BigDecimal.valueOf(0.01);
            default:
                return BigDecimal.ZERO;
        }
    }
}
