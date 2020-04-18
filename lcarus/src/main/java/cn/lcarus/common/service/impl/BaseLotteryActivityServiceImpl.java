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
import cn.hutool.core.util.StrUtil;
import cn.lcarus.admin.param.LotteryActivityAddParam;
import cn.lcarus.admin.param.LotteryActivityListParam;
import cn.lcarus.admin.res.LotteryActivityListVo;
import cn.lcarus.admin.res.WinUserInfoVo;
import cn.lcarus.common.entity.*;
import cn.lcarus.common.enums.*;
import cn.lcarus.common.mapper.LotteryActivityMapper;
import cn.lcarus.common.service.*;
import cn.lcarus.guess.web.param.LoginUserPageVo;
import cn.lcarus.guess.web.param.LotteryActivityDetailParam;
import cn.lcarus.guess.web.param.LotteryJoinUserDetailParam;
import cn.lcarus.guess.web.param.LotteryWinUserDetailParam;
import cn.lcarus.guess.web.res.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽奖活动 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Service
@Slf4j
public class BaseLotteryActivityServiceImpl extends BaseServiceImpl<LotteryActivityMapper, LotteryActivity> implements BaseLotteryActivityService {

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private BaseUserAccountRecordService baseUserAccountRecordService;

    @Autowired
    private BaseUserAccountService baseUserAccountService;

    @Autowired
    private BaseUserShareRecordService baseUserShareRecordService;

    @Autowired
    private BaseUserThirdInfoService baseUserThirdInfoService;

    @Autowired
    private BaseEcpmService baseEcpmService;

    @Autowired
    private BaseUserLotteryRecordService baseUserLotteryRecordService;

    @Autowired
    @Lazy
    private BaseLotteryActivityService baseLotteryActivityService;

    @Autowired
    private BaseConfigService baseConfigService;
    
    @Autowired
    private BaseUserAddressService baseUserAddressService;

    @Value("${aliyun.oss.root-url}")
    protected String OSS_ROOT_URL;

    @Value("${lcarus.wx.ad.video.join-lottery-adUnit-id}")
    private String joinLotteryAdUnitId;

    @Value("${wx.miniapp.appid}")
    private String miniAppId;

    @Override
    public ApiResult<LotteryActivityIndexRes> showIndex(LoginUserPageVo param) {
        Assert.notNull(param, "param");
        Long userId = param.getUserId();
        LotteryActivityIndexRes lotteryActivityIndexRes = new LotteryActivityIndexRes();
        DateTime now = DateUtil.date();
        // 封装活动列表信息
        List<LotteryActivity> records = lambdaQuery()
                .eq(LotteryActivity::getState, LotteryActivityStateEnum.WAIT_OPEN.getCode())
                .eq(LotteryActivity::getProductState, LotteryActivityProductStateEnum.UP.getCode())
                .gt(LotteryActivity::getOpenTime, DateUtil.date())
                .orderByAsc(LotteryActivity::getOpenTime)
                .list();
        if (CollUtil.isNotEmpty(records)) {
            Set<Long> collectLotteryActivityIds = records.stream().map(LotteryActivity::getId).collect(Collectors.toSet());
            Set<Long> userJoinLotterySet = baseUserLotteryRecordService.existUserLotteryRecord(userId, collectLotteryActivityIds);
            List<LotteryActivityListRes> collect = Lists.newLinkedList();
            List<LotteryActivityListRes> collectJoin = Lists.newLinkedList();
            for (LotteryActivity lotteryActivity : records) {
                LotteryActivityListRes lotteryActivityListRes = BeanUtil.toBean(lotteryActivity, LotteryActivityListRes.class);
                lotteryActivityListRes.setMainImageUrl(OSS_ROOT_URL + lotteryActivity.getMainImageUrl());
                lotteryActivityListRes.setDetailImageUrl(OSS_ROOT_URL + lotteryActivity.getDetailImageUrl());
                // 开奖剩余时间
                lotteryActivityListRes.setOpenSurplusTime(betweenOpenSurplusTimeMs(now, lotteryActivity.getOpenTime()));
                // 如果用户登录
                if (Objects.nonNull(userId)) {
                    boolean joinFlag = userJoinLotterySet.contains(lotteryActivity.getId());
                    lotteryActivityListRes.setJoinFlag(joinFlag);
                    if (joinFlag) {
                        collectJoin.add(lotteryActivityListRes);
                    } else {
                        collect.add(lotteryActivityListRes);
                    }
                } else {
                    collect.add(lotteryActivityListRes);
                }
            }
            collect.addAll(collectJoin);
            Page<LotteryActivityListRes> resPage = new Page<>();
            resPage.setTotal(collect.size());
            resPage.setRecords(collect);
            resPage.setSize(param.getSize());
            lotteryActivityIndexRes.setLotteryActivityList(resPage);
        }
        if (Objects.nonNull(userId)) {
            // 账户余额
            UserAccount userAccount = baseUserAccountService.getUserAccountAmount(userId);
            if (Objects.nonNull(userAccount)) {
                lotteryActivityIndexRes.setAccountAmount(userAccount.getAmount());
            }
            // 是否新人
            // boolean existUserLotteryRecord = baseUserLotteryRecordService.existUserLotteryRecord(userId);
            // lotteryActivityIndexRes.setNewUserFlag(!existUserLotteryRecord);
            // 分享限制
            String today = DateUtil.today();
            Integer count = baseUserShareRecordService.countUserShareRecord(userId, today);
            Integer limitCountDayUp = baseConfigService.getIntegerValue(ConfigEnum.LOTTERY_SHARE_LIMIT_COUNT_DAY_UP.key);
            if (count + 1 > limitCountDayUp) {
                lotteryActivityIndexRes.setShareLimitFlag(true);
            }
            // 是否展示提现按钮
            Boolean enableShowWithDraw = baseConfigService.getBooleanValue(ConfigEnum.WITH_DRAW_ENABLE_SHOW.key);
            lotteryActivityIndexRes.setWithDrawShowFlag(enableShowWithDraw);
            // 每次提现的最低限
            lotteryActivityIndexRes.setLimitAmountDown(getLimitAmountDown());
        }
        return ApiResult.ok(lotteryActivityIndexRes);
    }

    private BigDecimal getLimitAmountDown() {
        return BigDecimal.valueOf(baseConfigService.getLongValue(ConfigEnum.WITH_DRAW_LIMIT_AMOUNT_DOWN.key)).divide(BigDecimal.valueOf(CommonConstant.DEFAULT_100), CommonConstant.DEFAULT_2, RoundingMode.HALF_UP);
    }

    @Override
    public ApiResult<LotteryActivityDetailRes> showDetail(LotteryActivityDetailParam param) {
        // 校验
        ValidatorUtils.validateModel(param);
        Long lotteryActivityId = param.getLotteryActivityId();
        Long userId = param.getUserId();
        // 响应
        LotteryActivityDetailRes lotteryActivityDetailRes = new LotteryActivityDetailRes();
        // 抽奖活动信息
        LotteryActivity lotteryActivity = baseLotteryActivityService.getLotteryActivity(lotteryActivityId);
        if (Objects.isNull(lotteryActivity)) {
            return ApiResult.fail(ApiCode.ERROR_6100);
        }
        BeanUtil.copyProperties(lotteryActivity, lotteryActivityDetailRes);
        lotteryActivityDetailRes.setMainImageUrl(OSS_ROOT_URL + lotteryActivity.getMainImageUrl());
        lotteryActivityDetailRes.setDetailImageUrl(OSS_ROOT_URL + lotteryActivity.getDetailImageUrl());
        // 开奖剩余时间
        DateTime now = DateUtil.date();
        lotteryActivityDetailRes.setOpenSurplusTime(betweenOpenSurplusTimeMs(now, lotteryActivity.getOpenTime()));
        // 是否参与
        UserLotteryRecord userLotteryRecord = null;
        if (Objects.nonNull(userId)) {
            userLotteryRecord = baseUserLotteryRecordService.getUserLotteryRecord(lotteryActivityId, userId);
            // 是否参与
            if (Objects.nonNull(userLotteryRecord)) {
                lotteryActivityDetailRes.setJoinFlag(true);
                lotteryActivityDetailRes.setUserLotteryRecordId(userLotteryRecord.getId());
                // 是否中奖
                if (Objects.equals(userLotteryRecord.getState(), UserLotteryRecordStateEnum.WIN.getCode())) {
                    lotteryActivityDetailRes.setWinPrizeFlag(true);
                }
                // 中奖金额，只有红包才有
                if (Objects.equals(userLotteryRecord.getWinType(), LotteryActivityTypeEnum.RED_PACK.getCode())) {
                    lotteryActivityDetailRes.setWinningPrizeAmount(userLotteryRecord.getWinAmount());
                }
                lotteryActivityDetailRes.setWinType(userLotteryRecord.getWinType());
                lotteryActivityDetailRes.setInvalid(!baseUserLotteryRecordService.isValid(userLotteryRecord));
                // 判断是否领取
                lotteryActivityDetailRes.setGetFlag(userLotteryRecord.getGetFlag());
            }
        }
        // 中奖人集合
        List<UserShortInfoRes> selfWinUserList = dealWinUserCollection(lotteryActivity, userLotteryRecord);
        lotteryActivityDetailRes.setWinningPrizeUserList(selfWinUserList);
        // 参与人数
        Integer joinNum = baseUserLotteryRecordService.countJoin(lotteryActivityId);
        lotteryActivityDetailRes.setJoinNum(joinNum);
        // 参与人集合
        Page<UserLotteryRecord> joinUserLotteryRecordPageParam = new Page<>();
        joinUserLotteryRecordPageParam.setSize(DEFAULT_JOIN_SIZE);
        Page<UserLotteryRecord> joinUserLotteryRecordPage = baseUserLotteryRecordService.joinUserLotteryRecord(lotteryActivityId, joinUserLotteryRecordPageParam);
        List<UserShortInfoRes> joinUserList = getUserShortInfoRes(joinUserLotteryRecordPage.getRecords());
        lotteryActivityDetailRes.setJoinUserList(joinUserList);
        // 其余活动信息展示
        List<LotteryActivityListRes> otherLotteryActivityList = getOtherLotteryActivityIndexRes(userId);
        lotteryActivityDetailRes.setOtherLotteryActivityList(otherLotteryActivityList);
        // 广告地址处理
        lotteryActivityDetailRes.setAdUnitId(joinLotteryAdUnitId);
        return ApiResult.ok(lotteryActivityDetailRes);
    }

    /**
     * 获取其余活动信息展示
     *
     * @param userId
     * @return
     */
    private List<LotteryActivityListRes> getOtherLotteryActivityIndexRes(Long userId) {
        LoginUserPageVo<LotteryActivity> loginUserPageVo = new LoginUserPageVo<>();
        loginUserPageVo.setUserId(userId);
        loginUserPageVo.setSize(DEFAULT_SIZE);
        Page<LotteryActivity> notJoinLotteryActivityListPage = getNotJoinLotteryActivityList(loginUserPageVo);
        DateTime now = DateUtil.date();
        return notJoinLotteryActivityListPage.getRecords()
                .stream()
                .map(la -> {
                    LotteryActivityListRes lotteryActivityListRes = BeanUtil.toBean(la, LotteryActivityListRes.class);
                    lotteryActivityListRes.setMainImageUrl(OSS_ROOT_URL + lotteryActivityListRes.getMainImageUrl());
                    lotteryActivityListRes.setDetailImageUrl(OSS_ROOT_URL + lotteryActivityListRes.getDetailImageUrl());
                    // 开奖剩余时间
                    lotteryActivityListRes.setOpenSurplusTime(betweenOpenSurplusTimeMs(now, la.getOpenTime()));
                    return lotteryActivityListRes;
                })
                .collect(Collectors.toList());
    }

    /**
     * 中奖人记录查询
     *
     * @param lotteryActivity
     * @param userLotteryRecord
     */
    private List<UserShortInfoRes> dealWinUserCollection(LotteryActivity lotteryActivity, UserLotteryRecord userLotteryRecord) {
        if (Objects.isNull(lotteryActivity)) {
            return Collections.emptyList();
        }
        Long lotteryActivityId = lotteryActivity.getId();
        Page<UserLotteryRecord> winUserLotteryRecordPageParam = new Page<>();
        winUserLotteryRecordPageParam.setSize(DEFAULT_SIZE);
        Page<UserLotteryRecord> winUserLotteryRecordPage = baseUserLotteryRecordService.winUserLotteryRecord(lotteryActivityId, winUserLotteryRecordPageParam, lotteryActivity.getType());

        List<UserLotteryRecord> winRecords = winUserLotteryRecordPage.getRecords();
        if (CollUtil.isEmpty(winRecords)) {
            return Collections.emptyList();
        }
        if (Objects.isNull(userLotteryRecord) || !Objects.equals(userLotteryRecord.getState(), UserLotteryRecordStateEnum.WIN.getCode())) {
            // userLotteryRecord为空时直接返回
            return getUserShortInfoRes(winRecords);
        }
        winRecords.add(userLotteryRecord);
        List<UserShortInfoRes> winUserList = getUserShortInfoRes(winRecords);
        if (CollUtil.isNotEmpty(winUserList)) {
            Iterator<UserShortInfoRes> iterator = winUserList.iterator();
            UserShortInfoRes selfWinUserShortInfoRes = null;
            while (iterator.hasNext()) {
                UserShortInfoRes nextWinRecord = iterator.next();
                if (Objects.equals(nextWinRecord.getUserId(), userLotteryRecord.getUserId())) {
                    selfWinUserShortInfoRes = nextWinRecord;
                    iterator.remove();
                }
            }
            List<UserShortInfoRes> selfWinUserList = Lists.newArrayList(selfWinUserShortInfoRes);
            selfWinUserList.addAll(CollUtil.sub(winUserList, 0, DEFAULT_SIZE_TWO));
            return selfWinUserList;
        }
        return Collections.emptyList();
    }

    /**
     * 封装用户简略信息
     *
     * @param userLotteryRecords
     * @return
     */
    private List<UserShortInfoRes> getUserShortInfoRes(Collection<UserLotteryRecord> userLotteryRecords) {
        if (CollUtil.isEmpty(userLotteryRecords)) {
            return Collections.emptyList();
        }
        Map<Long, Integer> joinUserMap = new HashMap<>(userLotteryRecords.size());
        int i = 0;
        for (UserLotteryRecord userLotteryRecord : userLotteryRecords) {
            joinUserMap.put(userLotteryRecord.getUserId(), i++);
        }
        List<UserThirdInfo> userThirdInfoList = baseUserThirdInfoService.getUserThirdInfo(joinUserMap.keySet());
        return userThirdInfoList
                .stream()
                .map(userThirdInfo -> BeanUtil.toBean(userThirdInfo, UserShortInfoRes.class))
                .sorted(Comparator.comparing(v -> joinUserMap.get(v.getUserId())))
                .collect(Collectors.toList());
    }

    @Override
    public LotteryActivity getLotteryActivity(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, "lotteryActivityId");
        return getById(lotteryActivityId);
    }

    @Override
    public List<LotteryActivity> getLotteryActivityList(Collection<Long> lotteryActivityIdCollection) {
        if (CollUtil.isEmpty(lotteryActivityIdCollection)) {
            return Collections.emptyList();
        }
        return listByIds(lotteryActivityIdCollection);
    }

    @Override
    public Page<LotteryActivity> getNotJoinLotteryActivityList(LoginUserPageVo<LotteryActivity> param) {
        Assert.notNull(param, "param");
        Long userId = param.getUserId();
        return lambdaQuery()
                .eq(LotteryActivity::getState, LotteryActivityStateEnum.WAIT_OPEN.getCode())
                .eq(LotteryActivity::getProductState, LotteryActivityProductStateEnum.UP.getCode())
                .gt(LotteryActivity::getOpenTime, DateUtil.date())
                .notInSql(Objects.nonNull(userId), LotteryActivity::getId, StrUtil.format("SELECT ulr.lottery_activity_id FROM user_lottery_record ulr WHERE ulr.user_id = {}", userId))
                .orderByAsc(LotteryActivity::getOpenTime)
                .page(param);
    }

    @Override
    public ApiResult<LotteryWinUserDetailRes> winUserDetail(LotteryWinUserDetailParam param) {
        // 检验
        Assert.notNull(param, "param");
        Long userId = param.getUserId();
        Long lotteryActivityId = param.getLotteryActivityId();
        LotteryActivity lotteryActivity = baseLotteryActivityService.getLotteryActivity(lotteryActivityId);
        if (Objects.isNull(lotteryActivity)) {
            return ApiResult.fail(ApiCode.ERROR_6100);
        }
        LotteryWinUserDetailRes lotteryWinUserDetailRes = new LotteryWinUserDetailRes();
        // 分页查询中奖用户记录
        Page<UserLotteryRecord> winUserLotteryRecordPage = baseUserLotteryRecordService.winUserLotteryRecord(lotteryActivityId, param, lotteryActivity.getType());
        List<UserLotteryRecord> winRecords = winUserLotteryRecordPage.getRecords();
        if (CollUtil.isEmpty(winRecords)) {
            return ApiResult.ok(lotteryWinUserDetailRes);
        }
        List<UserShortInfoRes> winUserList = getUserShortInfoRes(winRecords);
        // 获取当前用户中奖记录
        UserLotteryRecord currentUserLotteryRecord = null;
        // 当前用户是否登录
        boolean isLogin = false;
        if (Objects.nonNull(userId)) {
            currentUserLotteryRecord = baseUserLotteryRecordService.winCurrentUserLotteryRecord(lotteryActivityId, userId);
            isLogin = true;
        }

        if (!isLogin || Objects.isNull(currentUserLotteryRecord)) {
            // 当前用户没有中奖
            Page<UserShortInfoRes> userShortInfoResPage = new Page<>();
            userShortInfoResPage.setTotal(winUserLotteryRecordPage.getTotal());
            userShortInfoResPage.setRecords(winUserList);
            userShortInfoResPage.setSize(param.getSize());
            lotteryWinUserDetailRes.setOtherWinUserPage(userShortInfoResPage);
            return ApiResult.ok(lotteryWinUserDetailRes);
        }
        // 当前用户已中奖
        List<UserShortInfoRes> winCurrentUserList = getUserShortInfoRes(Lists.newArrayList(currentUserLotteryRecord));
        if (CollUtil.isNotEmpty(winCurrentUserList)) {
            lotteryWinUserDetailRes.setCurrentWinUser(winCurrentUserList.get(0));
            Page<UserShortInfoRes> userShortInfoResPage = new Page<>();
            userShortInfoResPage.setTotal(winUserLotteryRecordPage.getTotal());
            UserLotteryRecord finalCurrentUserLotteryRecord = currentUserLotteryRecord;
            winUserList.removeIf(v -> Objects.equals(v.getUserId(), finalCurrentUserLotteryRecord.getUserId()));
            userShortInfoResPage.setRecords(winUserList);
            userShortInfoResPage.setSize(winUserLotteryRecordPage.getSize());
            lotteryWinUserDetailRes.setOtherWinUserPage(userShortInfoResPage);
        }
        return ApiResult.ok(lotteryWinUserDetailRes);
    }

    @Override
    public ApiResult<Page<UserShortInfoRes>> joinUserDetail(LotteryJoinUserDetailParam param) {
        // 检验
        Assert.notNull(param, "param");
        Long lotteryActivityId = param.getLotteryActivityId();
        // 分页查询参与用户记录
        Page<UserLotteryRecord> userLotteryRecordPage = baseUserLotteryRecordService.joinUserLotteryRecord(lotteryActivityId, param);
        List<UserLotteryRecord> records = userLotteryRecordPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return ApiResult.ok(new Page<>());
        }
        List<UserShortInfoRes> joinUserList = getUserShortInfoRes(records);
        Page<UserShortInfoRes> userShortInfoResPage = new Page<>();
        userShortInfoResPage.setTotal(userLotteryRecordPage.getTotal());
        userShortInfoResPage.setRecords(joinUserList);
        userShortInfoResPage.setSize(userLotteryRecordPage.getSize());
        return ApiResult.ok(userShortInfoResPage);
    }

    @Override
    public long betweenOpenSurplusTimeMs(Date now, Date openTime) {
        if (Objects.isNull(now) || Objects.isNull(openTime)) {
            return 0L;
        }
        if (DateUtil.compare(now, openTime) >= 0) {
            return 0L;
        }
        return DateUtil.betweenMs(now, openTime);
    }


    @Override
    public ApiResult<Page<LotteryActivityListVo>> showList(LotteryActivityListParam param) {
        LotteryActivityListVo lotteryActivityListVo = new LotteryActivityListVo();
        IPage<LotteryActivity> page = lambdaQuery()
                .like(StrUtil.isNotBlank(param.getName()), LotteryActivity::getName, param.getName())
                .eq(Objects.nonNull(param.getState()), LotteryActivity::getState, param.getState())
                .eq(Objects.nonNull(param.getLotteryActivityId()), LotteryActivity::getId, param.getLotteryActivityId())
                .ge(Objects.nonNull(param.getOpenTimeStart()), LotteryActivity::getOpenTime, param.getOpenTimeStart())
                .le(Objects.nonNull(param.getOpenTimeEnd()), LotteryActivity::getOpenTime, param.getOpenTimeEnd())
                .orderByDesc(LotteryActivity::getId)
                .page(param);
        List<LotteryActivity> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return ApiResult.ok();
        }
        List<LotteryActivityListVo> collect = records
                .stream()
                .map(record -> {
                    LotteryActivityListVo res = BeanUtil.toBean(record, LotteryActivityListVo.class);
                    res.setMainImageUrl(OSS_ROOT_URL + record.getMainImageUrl());
                    res.setDetailImageUrl(OSS_ROOT_URL + record.getDetailImageUrl());
                    // 抽奖参与人数，含机器人
                    Integer allJoinNum = baseUserLotteryRecordService.countAllJoinNum(record.getId());
                    res.setJoinAllNum(allJoinNum);
                    // 抽奖参与人数，不含机器人
                    Integer allMachineJoinNum = baseUserLotteryRecordService.countAllMachineJoinNum(record.getId());
                    if (Objects.nonNull(allJoinNum) && Objects.nonNull(allMachineJoinNum)) {
                        res.setJoinAllHumanNum(allJoinNum - allMachineJoinNum);
                        // 是否真人
                        if (allMachineJoinNum > 0) {
                            res.setHumanFlag(false);
                        }
                    }
                    // 中奖用户信息 只有实物中奖用户才有
                    if (Objects.equals(record.getType(), LotteryActivityTypeEnum.OBJECT.getCode())) {
                        List<UserLotteryRecord> winHumanUserLotteryRecords = baseUserLotteryRecordService.getWinHumanUserLotteryRecord(record.getId());
                        if (CollUtil.isNotEmpty(winHumanUserLotteryRecords)) {
                            List<WinUserInfoVo> winUserInfoVos = winHumanUserLotteryRecords
                                    .stream()
                                    .map(v -> {
                                        WinUserInfoVo winUserInfoVo = BeanUtil.toBean(v, WinUserInfoVo.class);
                                        // 昵称
                                        UserThirdInfo userThirdInfo = baseUserThirdInfoService.getUserThirdInfo(v.getUserId(), miniAppId);
                                        if (Objects.nonNull(userThirdInfo)) {
                                            BeanUtil.copyProperties(userThirdInfo, winUserInfoVo);
                                        }
                                        // 收货地址
                                        Long userAddressId = v.getUserAddressId();
                                        if (Objects.nonNull(userAddressId)) {
                                            UserAddress userAddress = baseUserAddressService.getById(userAddressId);
                                            if (Objects.nonNull(userAddress)) {
                                                winUserInfoVo.setUserAddress(userAddress.getProvinceName() + userAddress.getCityName() + userAddress.getCountyName() + userAddress.getDetailInfo());
                                                winUserInfoVo.setTelNumber(userAddress.getTelNumber());
                                                winUserInfoVo.setConsigneeName(userAddress.getConsigneeName());
                                            }
                                        }
                                        return winUserInfoVo;
                                    })
                                    .collect(Collectors.toList());
                            res.setWinUserInfoVos(winUserInfoVos);
                        }
                    }
                    return res;
                })
                .collect(Collectors.toList());
        Page<LotteryActivityListVo> resPage = new Page<>();
        resPage.setTotal(page.getTotal());
        resPage.setRecords(collect);
        resPage.setSize(page.getSize());
        return ApiResult.ok(resPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributeLock(value = "LOTTERY_ACTIVITY_ADMIN:ADD", expire = 360, timeout = 0, errMsg = "正在创建活动中，请不要频繁点击哦！")
    public ApiResult<Long> add(LotteryActivityAddParam param) {
        LotteryActivity lotteryActivitySave = BeanUtil.toBean(param, LotteryActivity.class);
        DateTime now = DateUtil.date();
        lotteryActivitySave.setCreateTime(now);
        lotteryActivitySave.setUpdateTime(now);
        lotteryActivitySave.setProductState(LotteryActivityProductStateEnum.DOWN.getCode());
        lotteryActivitySave.setState(LotteryActivityStateEnum.WAIT_ENABLE.getCode());
        boolean save = save(lotteryActivitySave);
        Assert.isTrue(save, ApiErrorCode.FAILED.getMsg());
        return ApiResult.ok(lotteryActivitySave.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributeLock(value = "LOTTERY_ACTIVITY_ADMIN:CHANGE_PRODUCT_STATE", key = "#lotteryActivityId", expire = 360, timeout = 0, errMsg = "正在处理中，请不要频繁点击哦！")
    public ApiResult<LotteryActivity> changeProductState(Long lotteryActivityId, Integer productState) {
        Assert.notNull(lotteryActivityId, "lotteryActivityId");
        Assert.notNull(productState, "productState");
        // 查询抽奖活动
        LotteryActivity lotteryActivity = getById(lotteryActivityId);
        if (Objects.isNull(lotteryActivity)) {
            return ApiResult.fail(ApiCode.CODE_100001);
        }
        DateTime now = DateUtil.date();
        LotteryActivity lotteryActivityUpdate = new LotteryActivity();
        lotteryActivityUpdate.setVersion(lotteryActivity.getVersion());
        lotteryActivityUpdate.setId(lotteryActivity.getId());
        lotteryActivityUpdate.setUpdateTime(now);
        if (Objects.equals(productState, LotteryActivityProductStateEnum.DOWN.getCode())) {
            // 下架
            if (Objects.equals(lotteryActivity.getProductState(), LotteryActivityProductStateEnum.DOWN.getCode())) {
                return ApiResult.ok();
            }
            if (Objects.equals(lotteryActivity.getState(), LotteryActivityStateEnum.WAIT_ENABLE.getCode())) {
                return ApiResult.ok();
            }
            lotteryActivityUpdate.setProductState(LotteryActivityProductStateEnum.DOWN.getCode());
            boolean update = updateById(lotteryActivityUpdate);
            Assert.isTrue(update, ApiErrorCode.FAILED.getMsg());
            return ApiResult.ok(lotteryActivity);
        } else if (Objects.equals(productState, LotteryActivityProductStateEnum.UP.getCode())) {
            // 上架
            if (Objects.equals(lotteryActivity.getProductState(), LotteryActivityProductStateEnum.UP.getCode())) {
                return ApiResult.ok();
            }
            if (!Objects.equals(lotteryActivity.getState(), LotteryActivityStateEnum.WAIT_ENABLE.getCode())) {
                return ApiResult.fail(ApiCode.CODE_100002);
            }
            if (DateUtil.compare(lotteryActivity.getOpenTime(), now) <= 0) {
                return ApiResult.fail(ApiCode.CODE_100003);
            }
            lotteryActivityUpdate.setState(LotteryActivityStateEnum.WAIT_OPEN.getCode());
            lotteryActivityUpdate.setProductState(LotteryActivityProductStateEnum.UP.getCode());
            lotteryActivityUpdate.setUpdateTime(now);
            lotteryActivityUpdate.setUpTime(now);
            boolean update = updateById(lotteryActivityUpdate);
            Assert.isTrue(update, ApiErrorCode.FAILED.getMsg());
            return ApiResult.ok(lotteryActivity);
        }
        return ApiResult.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public LotteryActivity getLotteryActivityNewEst(Long lotteryActivityId) {
        Assert.notNull(lotteryActivityId, "lotteryActivityId");
        return getById(lotteryActivityId);
    }
}
