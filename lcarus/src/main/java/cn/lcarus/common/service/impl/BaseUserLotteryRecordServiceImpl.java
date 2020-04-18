package cn.lcarus.common.service.impl;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.util.ValidatorUtils;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.lcarus.common.entity.LotteryActivity;
import cn.lcarus.common.entity.UserAddress;
import cn.lcarus.common.entity.UserLotteryRecord;
import cn.lcarus.common.enums.*;
import cn.lcarus.common.mapper.UserLotteryRecordMapper;
import cn.lcarus.common.service.BaseLotteryActivityService;
import cn.lcarus.common.service.BaseUserAccountService;
import cn.lcarus.common.service.BaseUserAddressService;
import cn.lcarus.common.service.BaseUserLotteryRecordService;
import cn.lcarus.guess.web.param.LotteryActivityJoinParam;
import cn.lcarus.guess.web.param.UserAfterOpenDrawParam;
import cn.lcarus.guess.web.param.UserLotteryRecordParam;
import cn.lcarus.guess.web.res.LotteryActivityJoinRes;
import cn.lcarus.guess.web.res.UserLotteryRecordRes;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户抽奖记录表 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Service
@Slf4j
public class BaseUserLotteryRecordServiceImpl extends BaseServiceImpl<UserLotteryRecordMapper, UserLotteryRecord> implements BaseUserLotteryRecordService {

    @Autowired
    private BaseLotteryActivityService baseLotteryActivityService;

    @Autowired
    @Lazy
    private BaseUserLotteryRecordService baseUserLotteryRecordService;

    @Autowired
    private BaseUserAccountService baseUserAccountService;

    @Autowired
    private BaseUserAddressService baseUserAddressService;

    @Value("${aliyun.oss.root-url}")
    protected String OSS_ROOT_URL;

    @Override
    public UserLotteryRecord getUserLotteryRecord(Long lotteryActivityId, Long userId) {
        Assert.notNull(userId, "userId");
        Assert.notNull(lotteryActivityId, "lotteryActivityId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getUserId, userId)
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .one();
    }

    @Override
    public UserLotteryRecord getAndCheckUserLotteryRecord(Long userLotteryRecordId, Long userId) {
        Assert.notNull(userId, "userId");
        Assert.notNull(userLotteryRecordId, "userLotteryRecordId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getUserId, userId)
                .eq(UserLotteryRecord::getId, userLotteryRecordId)
                .one();
    }

    @Override
    public boolean existUserLotteryRecord(Long lotteryActivityId, Long userId) {
        Assert.notNull(userId, "userId");
        Assert.notNull(lotteryActivityId, "lotteryActivityId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getUserId, userId)
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .count() > 0;
    }

    @Override
    public Set<Long> existUserLotteryRecord(Long userId, Collection<Long> lotteryActivityIds) {
        if (Objects.isNull(userId) || CollUtil.isEmpty(lotteryActivityIds)) {
            return Sets.newHashSet();
        }
        List<UserLotteryRecord> list = lambdaQuery()
                .eq(UserLotteryRecord::getUserId, userId)
                .in(UserLotteryRecord::getLotteryActivityId, lotteryActivityIds)
                .select(UserLotteryRecord::getLotteryActivityId, UserLotteryRecord::getUserId)
                .list();
        if (CollUtil.isEmpty(list)) {
            return Sets.newHashSet();
        }
        return list.stream()
                .map(UserLotteryRecord::getLotteryActivityId)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean existUserLotteryRecord(Long userId) {
        Assert.notNull(userId, "userId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getUserId, userId)
                .count() > 0;
    }

    @Override
    public Integer countJoin(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, "lotteryActivityId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .count();
    }

    @Override
    public Integer countUserJoin(Long userId) {
        Assert.notNull(userId, "userId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getUserId, userId)
                .count();
    }

    @Override
    public Integer countUserWin(Long userId) {
        Assert.notNull(userId, "userId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getUserId, userId)
                .eq(UserLotteryRecord::getState, UserLotteryRecordStateEnum.WIN.getCode())
                .count();
    }

    @Override
    public Page<UserLotteryRecord> joinUserLotteryRecord(Long lotteryActivityId, Page<UserLotteryRecord> page) {
        Assert.notNull(lotteryActivityId, "lotteryActivityId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .orderByDesc(UserLotteryRecord::getId)
                .page(page);
    }

    @Override
    public Page<UserLotteryRecord> winUserLotteryRecord(Long lotteryActivityId, Page<UserLotteryRecord> page) {
        Assert.notNull(lotteryActivityId, "lotteryActivityId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .eq(UserLotteryRecord::getState, UserLotteryRecordStateEnum.WIN.getCode())
                .orderByDesc(UserLotteryRecord::getId)
                .page(page);
    }

    @Override
    public Page<UserLotteryRecord> winUserLotteryRecord(Long lotteryActivityId, Page<UserLotteryRecord> page, Integer winType) {
        Assert.notNull(lotteryActivityId, "lotteryActivityId");
        Assert.notNull(page, "page");
        Assert.notNull(winType, "winType");

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .eq(UserLotteryRecord::getState, UserLotteryRecordStateEnum.WIN.getCode())
                .eq(UserLotteryRecord::getWinType, winType)
                .orderByDesc(UserLotteryRecord::getId)
                .page(page);
    }

    @Override
    public UserLotteryRecord winCurrentUserLotteryRecord(Long lotteryActivityId, Long userId) {
        Assert.notNull(lotteryActivityId, "lotteryActivityId");
        Assert.notNull(userId, "userId");

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .eq(UserLotteryRecord::getUserId, userId)
                .eq(UserLotteryRecord::getState, UserLotteryRecordStateEnum.WIN.getCode())
                .one();
    }

    @Override
    public ApiResult<Page<UserLotteryRecordRes>> showRecordList(UserLotteryRecordParam param) {
        Assert.notNull(param, "param");
        ValidatorUtils.validateModel(param);
        Long userId = param.getUserId();
        String queryType = param.getQueryType();
        Assert.notNull(userId, "userId");

        LambdaQueryWrapper<UserLotteryRecord> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(UserLotteryRecord::getUserId, userId)
                .orderByDesc(UserLotteryRecord::getId);
        switch (queryType) {
            case WIN_TYPE:
                // 中奖记录查询
                queryWrapper.in(UserLotteryRecord::getState, UserLotteryRecordStateEnum.WIN.getCode());
                break;
            case JOIN_TYPE:
                // 抽奖记录查询
                break;
            default:
                return ApiResult.ok(new Page<>());
        }
        UserLotteryRecordParam page = page(param, queryWrapper);
        List<UserLotteryRecord> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return ApiResult.ok(new Page<>());
        }
        List<Long> lotteryActivityIdList = records
                .stream()
                .map(UserLotteryRecord::getLotteryActivityId)
                .collect(Collectors.toList());
        List<LotteryActivity> lotteryActivityList = baseLotteryActivityService.getLotteryActivityList(lotteryActivityIdList);
        Map<Long, UserLotteryRecordRes> lotteryActivityIdToRecordResMap = lotteryActivityList
                .stream()
                .map(lotteryActivity -> {
                    UserLotteryRecordRes userLotteryRecordRes = BeanUtil.toBean(lotteryActivity, UserLotteryRecordRes.class);
                    userLotteryRecordRes.setLotteryActivityId(lotteryActivity.getId());
                    userLotteryRecordRes.setMainImageUrl(OSS_ROOT_URL + lotteryActivity.getMainImageUrl());
                    userLotteryRecordRes.setDetailImageUrl(OSS_ROOT_URL + lotteryActivity.getDetailImageUrl());
                    return userLotteryRecordRes;
                })
                .collect(Collectors.toMap(UserLotteryRecordRes::getLotteryActivityId, v -> v, (v1, v2) -> v2));
        List<UserLotteryRecordRes> collect = records
                .stream()
                .filter(record -> lotteryActivityIdToRecordResMap.containsKey(record.getLotteryActivityId()))
                .map(record -> {
                    UserLotteryRecordRes userLotteryRecordRes = lotteryActivityIdToRecordResMap.get(record.getLotteryActivityId());
                    userLotteryRecordRes.setWinTime(record.getWinTime());
                    userLotteryRecordRes.setWinType(record.getWinType());
                    userLotteryRecordRes.setGetFlag(record.getGetFlag());
                    userLotteryRecordRes.setUserLotteryRecordState(record.getState());
                    return userLotteryRecordRes;
                }).collect(Collectors.toList());
        Page<UserLotteryRecordRes> pageRes = new Page<>();
        pageRes.setRecords(collect);
        pageRes.setTotal(page.getTotal());
        return ApiResult.ok(pageRes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributeLock(value = "LOTTERY_ACTIVITY:JOIN", key = "#param.userId + ':' + #param.lotteryActivityId", expire = 360, timeout = 0, errMsg = "正在参与抽奖中，请不要频繁点击哦！")
    public ApiResult<LotteryActivityJoinRes> join(LotteryActivityJoinParam param) {
        // 校验
        ValidatorUtils.validateModel(param);
        Long lotteryActivityId = param.getLotteryActivityId();
        Long userId = param.getUserId();
        Assert.notNull(userId, "userId");
        // 获取活动信息
        LotteryActivity lotteryActivity = baseLotteryActivityService.getLotteryActivity(lotteryActivityId);
        ApiResult<LotteryActivityJoinRes> checkJoinResult = checkJoin(lotteryActivityId, userId, lotteryActivity);
        if (checkJoinResult.isFail()) {
            return checkJoinResult;
        }
        // 封装返回响应
        LotteryActivityJoinRes lotteryActivityJoinRes = BeanUtil.toBean(lotteryActivity, LotteryActivityJoinRes.class);
        lotteryActivityJoinRes.setLotteryActivityId(lotteryActivity.getId());
        // 参与抽奖逻辑
        UserLotteryRecord userLotteryRecordSave = new UserLotteryRecord();
        userLotteryRecordSave.setCreateTime(DateUtil.date());
        userLotteryRecordSave.setUpdateTime(userLotteryRecordSave.getCreateTime());
        userLotteryRecordSave.setLotteryActivityId(lotteryActivityId);
        userLotteryRecordSave.setUserId(userId);
        userLotteryRecordSave.setState(UserLotteryRecordStateEnum.JOIN.getCode());
        boolean save = baseUserLotteryRecordService.save(userLotteryRecordSave);
        Assert.isTrue(save);
        return ApiResult.ok(lotteryActivityJoinRes);
    }

    @Override
    @DistributeLock(value = "USER_AFTER_OPEN_RECORD:DREW", key = "#param.userId + ':' + #param.userLotteryRecordId", expire = 360, timeout = 0, errMsg = "正在领取中，请不要频繁点击哦！")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> draw(UserAfterOpenDrawParam param) {
        // 参数校验
        Assert.notNull(param, "param");
        ValidatorUtils.validateModel(param);
        Long userId = param.getUserId();
        Long userLotteryRecordId = param.getUserLotteryRecordId();
        Assert.notNull(userId, "userId");

        UserLotteryRecord userLotteryRecord = baseUserLotteryRecordService.getAndCheckUserLotteryRecord(userLotteryRecordId, userId);
        if (Objects.isNull(userLotteryRecord)) {
            return ApiResult.fail(ApiCode.ERROR_6104);
        }
        if (Objects.equals(userLotteryRecord.getState(), UserLotteryRecordStateEnum.JOIN.getCode())) {
            return ApiResult.fail(ApiCode.ERROR_6106);
        }
        if (userLotteryRecord.getGetFlag()) {
            return ApiResult.fail(ApiCode.ERROR_6105);
        }
        DateTime now = DateUtil.date();
        if (!baseUserLotteryRecordService.isValid(userLotteryRecord)) {
            return ApiResult.fail(ApiCode.ERROR_6204);
        }
        // 领取逻辑
        // 用户账户变更，只有红包类型才变更
        BigDecimal winAmount = userLotteryRecord.getWinAmount();
        Long userAccountAmountRecordId = null;
        Long userAddressId = null;
        if (Objects.equals(userLotteryRecord.getWinType(), LotteryActivityTypeEnum.RED_PACK.getCode())) {
            userAccountAmountRecordId = baseUserAccountService.changeUserAccountAmount(userId, winAmount, UserAccountUseTypeEnum.RED_PACK.getCode());
        } else if (Objects.equals(userLotteryRecord.getWinType(), LotteryActivityTypeEnum.OBJECT.getCode())) {
            // 用户地址检测
            if (Objects.isNull(param.getUserAddressId())) {
                return ApiResult.fail(ApiCode.ERROR_6501);
            }
            UserAddress userAddress = baseUserAddressService.getAndCheckUserAddress(userId, param.getUserAddressId());
            if (Objects.isNull(userAddress)) {
                return ApiResult.fail(ApiCode.ERROR_6500);
            }
            userAddressId = param.getUserAddressId();
        }
        // 保存领取记录
        UserLotteryRecord userLotteryRecordUpdate = new UserLotteryRecord();
        userLotteryRecordUpdate.setId(userLotteryRecord.getId());
        userLotteryRecordUpdate.setGetFlag(true);
        userLotteryRecordUpdate.setVersion(userLotteryRecord.getVersion());
        userLotteryRecordUpdate.setUpdateTime(now);
        userLotteryRecordUpdate.setUserAccountRecordId(userAccountAmountRecordId);
        userLotteryRecordUpdate.setUserAddressId(userAddressId);
        boolean update = baseUserLotteryRecordService.updateById(userLotteryRecordUpdate);
        Assert.isTrue(update);
        return ApiResult.ok();
    }

    /**
     * 参与抽奖条件检测
     *
     * @param lotteryActivityId
     * @param userId
     * @param lotteryActivity
     * @return
     */
    private ApiResult<LotteryActivityJoinRes> checkJoin(Long lotteryActivityId, Long userId, LotteryActivity lotteryActivity) {
        if (Objects.isNull(lotteryActivity)) {
            return ApiResult.fail(ApiCode.ERROR_6100);
        }
        if (!Objects.equals(lotteryActivity.getState(), LotteryActivityStateEnum.WAIT_OPEN.getCode())
                || !Objects.equals(lotteryActivity.getProductState(), LotteryActivityProductStateEnum.UP.getCode())
                || DateUtil.compare(lotteryActivity.getOpenTime(), DateUtil.date()) <= 0) {
            return ApiResult.fail(ApiCode.ERROR_6102);
        }
        // 获取活动记录
        boolean joinFlag = baseUserLotteryRecordService.existUserLotteryRecord(lotteryActivityId, userId);
        if (joinFlag) {
            return ApiResult.fail(ApiCode.ERROR_6103);
        }
        return ApiResult.ok();
    }

    @Override
    public boolean isValid(UserLotteryRecord userLotteryRecord) {
        if (Objects.isNull(userLotteryRecord)) {
            return true;
        }
        Date winTime = userLotteryRecord.getWinTime();
        winTime = winTime == null ? userLotteryRecord.getCreateTime() : winTime;
        return DateUtil.isIn(DateUtil.date(), winTime, DateUtil.offsetDay(winTime, CommonConstant.DEFAULT_TWO_DAY));
    }

    @Override
    public Integer countAllJoinNum(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, ApiCode.CODE_100000.getMsg());

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .count();
    }

    @Override
    public Integer countAllMachineJoinNum(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, ApiCode.CODE_100000.getMsg());

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .eq(UserLotteryRecord::getRobotFlag, true)
                .count();
    }

    @Override
    public Integer countAllHumanJoinNum(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, ApiCode.CODE_100000.getMsg());
        return countAllJoinNum(lotteryActivityId) - countAllMachineJoinNum(lotteryActivityId);
    }

    @Override
    public List<UserLotteryRecord> getWinHumanUserLotteryRecord(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, ApiCode.CODE_100000.getMsg());

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .eq(UserLotteryRecord::getState, UserLotteryRecordStateEnum.WIN.getCode())
                .eq(UserLotteryRecord::getRobotFlag, false)
                .list();
    }

    @Override
    public List<UserLotteryRecord> getJoinUserLotteryRecord(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, ApiCode.CODE_100000.getMsg());

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .list();
    }

    @Override
    public List<UserLotteryRecord> getJoinHumanUserLotteryRecord(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, ApiCode.CODE_100000.getMsg());

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .eq(UserLotteryRecord::getRobotFlag, false)
                .list();
    }

    @Override
    public List<UserLotteryRecord> getJoinMachineUserLotteryRecord(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, ApiCode.CODE_100000.getMsg());

        return lambdaQuery()
                .eq(UserLotteryRecord::getLotteryActivityId, lotteryActivityId)
                .eq(UserLotteryRecord::getRobotFlag, true)
                .list();
    }
}
