package cn.chinotan.zeus.delay;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.config.constant.DelayType;
import cn.chinotan.zeus.framework.util.RedPacketUtil;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.lcarus.common.entity.LotteryActivity;
import cn.lcarus.common.entity.UserLotteryRecord;
import cn.lcarus.common.enums.LotteryActivityStateEnum;
import cn.lcarus.common.enums.LotteryActivityTypeEnum;
import cn.lcarus.common.enums.UserLotteryRecordStateEnum;
import cn.lcarus.common.service.BaseEcpmService;
import cn.lcarus.common.service.BaseLotteryActivityService;
import cn.lcarus.common.service.BaseUserLotteryRecordService;
import cn.lcarus.common.service.BaseWxService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @program: lcarus
 * @description: 抽奖开奖延时任务处理
 * @author: xingcheng
 * @create: 2020-03-07 11:19
 **/
@Slf4j
@Service(DelayType.LOTTERY_OPEN + RedisDelayHandler.REDIS_DELAY_HANDLER)
public class LotteryOpenDelayHandler implements RedisDelayHandler<Long> {

    @Autowired
    private BaseLotteryActivityService baseLotteryActivityService;

    @Autowired
    private BaseEcpmService baseEcpmService;

    @Autowired
    private BaseUserLotteryRecordService baseUserLotteryRecordService;

    @Autowired
    private BaseWxService baseWxService;

    public static final Integer DEFAULT_NUM = 1000;

    public static final Integer DEFAULT_100 = 100;

    public static final Integer DEFAULT_SCALE = 2;

    public static final Integer DEFAULT_MIN_AMOUNT = 1;

    public static final BigDecimal DEFAULT_DISTRIBUTION_MIN_AMOUNT = BigDecimal.valueOf(0.01);

    @Override
    @Async
    @DistributeLock(value = "LOTTERY_ACTIVITY:OPEN_DELAY_DEAL", key = "#lotteryActivityId", expire = 1024, timeout = 0, errMsg = "正在开奖处理中！")
    @Retryable(value = {Exception.class}, exclude = {IllegalStateException.class}, maxAttempts = 10, backoff = @Backoff(delay = 2000L, multiplier = 3))
    @Transactional(rollbackFor = Exception.class)
    public void delayExecute(Long lotteryActivityId) {
        Asserts.notNull(lotteryActivityId, "lotteryActivityId");
        LotteryActivity lotteryActivity = baseLotteryActivityService.getLotteryActivityNewEst(lotteryActivityId);
        if (Objects.isNull(lotteryActivity)) {
            log.error("LotteryOpenDelayHandler-delayExecute lotteryActivity is null lotteryActivityId: {}", lotteryActivityId);
            return;
        }
        log.info("LotteryOpenDelayHandler-delayExecute lotteryActivity is: {}", JSON.toJSONString(lotteryActivity));
        // 校验活动状态，只处理待开奖的
        if (!Objects.equals(lotteryActivity.getState(), LotteryActivityStateEnum.WAIT_OPEN.getCode())) {
            log.error("LotteryOpenDelayHandler-delayExecute lotteryActivity state is not wait_open lotteryActivityId: {}, state: {}", lotteryActivityId, lotteryActivity.getState());
            return;
        }
        DateTime now = DateUtil.date();
        // 进行开奖逻辑
        // 1.中奖规则计算
        Integer type = lotteryActivity.getType();
        LotteryActivityTypeEnum lotteryActivityTypeEnum = LotteryActivityTypeEnum.codeOf(type);
        if (Objects.isNull(lotteryActivityTypeEnum)) {
            log.error("LotteryOpenDelayHandler-delayExecute lotteryActivityTypeEnum is invalid lotteryActivity: {}", JSON.toJSONString(lotteryActivity));
            return;
        }
        // 获取抽奖参数人数（不含机器人）
        List<UserLotteryRecord> joinHumanUserLotteryRecordList = baseUserLotteryRecordService.getJoinHumanUserLotteryRecord(lotteryActivityId);
        // 获取抽奖参数人数（机器人）
        List<UserLotteryRecord> joinMachineUserLotteryRecordList = baseUserLotteryRecordService.getJoinMachineUserLotteryRecord(lotteryActivityId);
        boolean existHumanUserLotteryRecord = CollUtil.isNotEmpty(joinHumanUserLotteryRecordList);
        boolean existMachineUserLotteryRecord = CollUtil.isNotEmpty(joinMachineUserLotteryRecordList);
        if (!existHumanUserLotteryRecord && !existMachineUserLotteryRecord) {
            log.info("LotteryOpenDelayHandler-delayExecute joinUserLotteryRecordList isEmpty lotteryActivity: {}", JSON.toJSONString(lotteryActivity));
            return;
        }
        // 获取ecpm配置
        BigDecimal ecpmValue = baseEcpmService.getEcpmValue();
        // 真人参与人数
        int allHumanJoinNum = 0;
        // 奖池金额 元
        BigDecimal prizePoolAmount;
        // 奖池原始金额 分
        int prizePoolOriginAmountFen = 0;
        Set<Long> waitSendNoticeUserIdSet;
        if (existHumanUserLotteryRecord) {
            allHumanJoinNum = joinHumanUserLotteryRecordList.size();
            prizePoolAmount = ecpmValue.multiply(BigDecimal.valueOf(allHumanJoinNum)).divide(BigDecimal.valueOf(DEFAULT_NUM), DEFAULT_SCALE, RoundingMode.HALF_UP);
            prizePoolOriginAmountFen = prizePoolAmount.multiply(BigDecimal.valueOf(DEFAULT_100)).intValue();
            waitSendNoticeUserIdSet = new HashSet<>(allHumanJoinNum);
        } else {
            waitSendNoticeUserIdSet = new HashSet<>();
            prizePoolAmount = BigDecimal.ZERO;
        }
        switch (lotteryActivityTypeEnum) {
            case OBJECT:
                // 实物抽奖
                // 实物中奖人数=实物奖品数量
                int lotteryPrizeNum = lotteryActivity.getNum();
                // 实物奖品成本 分 单价 * 数量
                int costTotalPriceFen = lotteryActivity.getCostPrice().multiply(BigDecimal.valueOf(DEFAULT_100)).intValue() * lotteryPrizeNum;
                // 奖池金额重新分配 分 奖池原始金额 - 实物奖品成本
                int costPrizePoolAmountFen = prizePoolOriginAmountFen - costTotalPriceFen;
                // 人均最低奖励总额 分 参与抽奖人数 * 1
                int totalMinimumRewardPer = allHumanJoinNum * DEFAULT_MIN_AMOUNT;
                // 除实物中奖外的得到红包的人数
                int redPackNum = allHumanJoinNum - lotteryPrizeNum;
                // 是否真人中奖
                boolean humanWin = costPrizePoolAmountFen > totalMinimumRewardPer;
                if (humanWin) {
                    // 真人中奖
                    // 实物奖励分配
                    List<Integer> joinList = new ArrayList<>(allHumanJoinNum);
                    for (int i = 0; i < allHumanJoinNum; i++) {
                        joinList.add(i);
                    }
                    int size = joinList.size();
                    if (lotteryPrizeNum > size) {
                        lotteryPrizeNum = size;
                    }
                    // 获得实物奖品的参与者序号
                    Set<Integer> winObjectSet = RandomUtil.randomEleSet(joinList, lotteryPrizeNum);
                    if (CollUtil.isNotEmpty(winObjectSet)) {
                        for (int index : winObjectSet) {
                            if (index < allHumanJoinNum) {
                                UserLotteryRecord userLotteryRecordUpdate = joinHumanUserLotteryRecordList.get(index);
                                if (Objects.nonNull(userLotteryRecordUpdate)) {
                                    waitSendNoticeUserIdSet.add(userLotteryRecordUpdate.getUserId());
                                    userLotteryRecordUpdate.setUpdateTime(now);
                                    userLotteryRecordUpdate.setWinType(LotteryActivityTypeEnum.OBJECT.getCode());
                                    userLotteryRecordUpdate.setState(UserLotteryRecordStateEnum.WIN.getCode());
                                    userLotteryRecordUpdate.setWinTime(now);
                                }
                            }
                        }
                    }
                    // 红包奖励分配
                    distributeRedPack(lotteryActivity, now, joinHumanUserLotteryRecordList, joinMachineUserLotteryRecordList
                            , existHumanUserLotteryRecord, existMachineUserLotteryRecord, allHumanJoinNum, waitSendNoticeUserIdSet, costPrizePoolAmountFen, redPackNum, UserLotteryRecordStateEnum.NO_WIN.getCode());
                } else {
                    // 机器人中奖
                    // 查询机器人中奖记录
                    if (!existMachineUserLotteryRecord) {
                        // 插入机器人中奖记录
                        saveMachineWinRecord(lotteryActivityId, now, lotteryPrizeNum);
                    } else {
                        // 参加的机器人数量
                        Set<Integer> joinMachineUserLotteryRecordSet = new HashSet<>(lotteryPrizeNum);
                        int i = 0;
                        for (UserLotteryRecord userLotteryRecord : joinMachineUserLotteryRecordList) {
                            joinMachineUserLotteryRecordSet.add(i++);
                        }
                        int joinMachineUserLotteryRecordNum = joinMachineUserLotteryRecordList.size();
                        if (lotteryPrizeNum > joinMachineUserLotteryRecordNum) {
                            lotteryPrizeNum = joinMachineUserLotteryRecordNum;
                        }
                        Set<Integer> robotUserIdSet = RandomUtil.randomEleSet(joinMachineUserLotteryRecordSet, lotteryPrizeNum);
                        List<UserLotteryRecord> userLotteryRecordUpdateList = new ArrayList<>(lotteryPrizeNum);
                        if (CollUtil.isNotEmpty(robotUserIdSet)) {
                            for (Integer robotIndexId : robotUserIdSet) {
                                UserLotteryRecord userMachineLotteryRecordUpdate = joinMachineUserLotteryRecordList.get(robotIndexId);
                                if (Objects.nonNull(userMachineLotteryRecordUpdate)) {
                                    userMachineLotteryRecordUpdate.setUpdateTime(now);
                                    userMachineLotteryRecordUpdate.setWinType(LotteryActivityTypeEnum.OBJECT.getCode());
                                    userMachineLotteryRecordUpdate.setState(UserLotteryRecordStateEnum.WIN.getCode());
                                    userMachineLotteryRecordUpdate.setWinTime(now);
                                    userMachineLotteryRecordUpdate.setRobotFlag(true);
                                }
                            }
                        }
                    }

                    // 红包奖励分配
                    distributeRedPack(lotteryActivity, now, joinHumanUserLotteryRecordList, joinMachineUserLotteryRecordList
                            , existHumanUserLotteryRecord, existMachineUserLotteryRecord, allHumanJoinNum, waitSendNoticeUserIdSet, prizePoolOriginAmountFen, allHumanJoinNum, UserLotteryRecordStateEnum.NO_WIN.getCode());
                }
                break;
            case RED_PACK:
                // 红包抽奖分配
                log.info("LotteryOpenDelayHandler-delayExecute red_pack prizePoolAmount is {}, allHumanJoinNum is {}, ecpmValue is {}, lotteryActivity is {}"
                        , prizePoolAmount.toString(), allHumanJoinNum, ecpmValue.toString(), JSON.toJSONString(lotteryActivity));
                distributeRedPack(lotteryActivity, now, joinHumanUserLotteryRecordList, joinMachineUserLotteryRecordList
                        , existHumanUserLotteryRecord, existMachineUserLotteryRecord, allHumanJoinNum, waitSendNoticeUserIdSet, prizePoolOriginAmountFen, allHumanJoinNum, UserLotteryRecordStateEnum.WIN.getCode());
                break;
            default:
                // 暂不支持
                log.error("LotteryOpenDelayHandler-delayExecute lotteryActivityTypeEnum is not support lotteryActivity: {}", JSON.toJSONString(lotteryActivity));
                return;
        }
        // 2.更新用户中奖记录
        if (existHumanUserLotteryRecord) {
            boolean updateBatchHuman = baseUserLotteryRecordService.updateBatchById(joinHumanUserLotteryRecordList);
            Assert.isTrue(updateBatchHuman);
        }
        if (existMachineUserLotteryRecord) {
            boolean updateBatchMachine = baseUserLotteryRecordService.updateBatchById(joinMachineUserLotteryRecordList);
            Assert.isTrue(updateBatchMachine);
        }
        // 3.开奖完成后，更新活动状态为已结束
        LotteryActivity lotteryActivityUpdate = new LotteryActivity();
        lotteryActivityUpdate.setId(lotteryActivity.getId());
        lotteryActivityUpdate.setVersion(lotteryActivity.getVersion());
        lotteryActivityUpdate.setState(LotteryActivityStateEnum.END.getCode());
        lotteryActivityUpdate.setUpdateTime(now);
        boolean updateLotteryActivity = baseLotteryActivityService.updateById(lotteryActivityUpdate);
        Assert.isTrue(updateLotteryActivity);
        // 4.发送用户开奖通知 异步 
        if (existHumanUserLotteryRecord) {
            baseWxService.sendOpenLotteryNotice(lotteryActivity, now, waitSendNoticeUserIdSet);
        }
    }

    private void distributeRedPack(LotteryActivity lotteryActivity, DateTime now, List<UserLotteryRecord> joinHumanUserLotteryRecordList
            , List<UserLotteryRecord> joinMachineUserLotteryRecordList, boolean existHumanUserLotteryRecord, boolean existMachineUserLotteryRecord
            , int allHumanJoinNum, Set<Long> waitSendNoticeUserIdSet, int costPrizePoolAmountFen, int redPackNum, Integer userLotteryRecordState) {
        // 剩余真人人数红包奖励分配
        if (existHumanUserLotteryRecord) {
            distributeRedPack(lotteryActivity, now, joinHumanUserLotteryRecordList, redPackNum, allHumanJoinNum, costPrizePoolAmountFen, waitSendNoticeUserIdSet, userLotteryRecordState);
        }
        if (existMachineUserLotteryRecord) {
            // 剩余机器人红包分配
            distributeMachineRedPack(lotteryActivity, now, joinMachineUserLotteryRecordList, userLotteryRecordState);
        }
    }

    /**
     * 保存机器人中奖记录
     *
     * @param lotteryActivityId
     * @param now
     * @param lotteryPrizeNum
     */
    private void saveMachineWinRecord(Long lotteryActivityId, DateTime now, int lotteryPrizeNum) {
        List<Long> defaultRobotUserIdSet = CommonConstant.DEFAULT_ROBOT_USER_ID;
        int size = defaultRobotUserIdSet.size();
        if (lotteryPrizeNum > size) {
            lotteryPrizeNum = size;
        }
        List<UserLotteryRecord> userLotteryRecordSaveList = new ArrayList<>(lotteryPrizeNum);
        UserLotteryRecord userLotteryRecordSave;
        Set<Long> robotUserIdSet = RandomUtil.randomEleSet(defaultRobotUserIdSet, lotteryPrizeNum);
        Iterator<Long> robotUserIdIterator = robotUserIdSet.iterator();
        for (int i = 0; i < lotteryPrizeNum; i++) {
            if (robotUserIdIterator.hasNext()) {
                userLotteryRecordSave = new UserLotteryRecord();
                userLotteryRecordSave.setWinType(LotteryActivityTypeEnum.OBJECT.getCode());
                userLotteryRecordSave.setUserId(robotUserIdIterator.next());
                userLotteryRecordSave.setUpdateTime(now);
                userLotteryRecordSave.setCreateTime(now);
                userLotteryRecordSave.setLotteryActivityId(lotteryActivityId);
                userLotteryRecordSave.setState(UserLotteryRecordStateEnum.WIN.getCode());
                userLotteryRecordSave.setGetFlag(true);
                userLotteryRecordSave.setRobotFlag(true);
                userLotteryRecordSaveList.add(userLotteryRecordSave);
            }
        }
        // 保存机器人抽奖
        boolean saveUserLotteryRecord = baseUserLotteryRecordService.saveBatch(userLotteryRecordSaveList);
        Assert.isTrue(saveUserLotteryRecord);
    }


    /**
     * @param lotteryActivity           活动信息
     * @param now                       当前时间
     * @param joinUserLotteryRecordList 所以参与者的抽奖记录
     * @param distributeRedPackNum      要分配的红包数量
     * @param allHumanJoinNum           所以的参与者数量
     * @param prizePoolAmountFen        奖池金额
     * @param waitSendNoticeUserIdSet   待通知的用户id
     * @param userLotteryRecordState    用户抽奖记录状态
     */
    private void distributeRedPack(LotteryActivity lotteryActivity, DateTime now, List<UserLotteryRecord> joinUserLotteryRecordList, int distributeRedPackNum,
                                   int allHumanJoinNum, int prizePoolAmountFen, Set<Long> waitSendNoticeUserIdSet, Integer userLotteryRecordState) {
        if (CollUtil.isEmpty(joinUserLotteryRecordList)) {
            log.warn("LotteryOpenDelayHandler-distributeRedPack joinUserLotteryRecordList is empty lotteryActivity: {}", JSON.toJSONString(lotteryActivity));
            return;
        }
        List<Integer> distributionAmountList = RedPacketUtil.splitRedPackets(prizePoolAmountFen, distributeRedPackNum, DEFAULT_MIN_AMOUNT, prizePoolAmountFen);
        boolean isDistribution = true;
        if (CollUtil.isEmpty(distributionAmountList)) {
            // 金额无法分配时，均分配1分
            log.warn("LotteryOpenDelayHandler-delayExecute splitRedPackets distributionAmountList isEmpty prizePoolAmount {}, distributeRedPackNum {}, allHumanJoinNum {}, lotteryActivity {}"
                    , prizePoolAmountFen / DEFAULT_100, distributeRedPackNum, allHumanJoinNum, JSON.toJSONString(lotteryActivity));
            isDistribution = false;
        }
        for (int i = 0; i < allHumanJoinNum; i++) {
            UserLotteryRecord userLotteryRecordUpdate = joinUserLotteryRecordList.get(i);
            if (Objects.nonNull(userLotteryRecordUpdate)) {
                waitSendNoticeUserIdSet.add(userLotteryRecordUpdate.getUserId());
                if (Objects.isNull(userLotteryRecordUpdate.getWinType())) {
                    userLotteryRecordUpdate.setUpdateTime(now);
                    userLotteryRecordUpdate.setWinType(LotteryActivityTypeEnum.RED_PACK.getCode());
                    userLotteryRecordUpdate.setState(userLotteryRecordState);
                    userLotteryRecordUpdate.setWinTime(now);
                    Integer distributionAmountFen = distributionAmountList.get(i);
                    if (isDistribution && Objects.nonNull(distributionAmountFen)) {
                        BigDecimal distributionAmountYuan = BigDecimal.valueOf(distributionAmountFen).divide(BigDecimal.valueOf(DEFAULT_100), DEFAULT_SCALE, RoundingMode.HALF_UP);
                        userLotteryRecordUpdate.setWinAmount(distributionAmountYuan);
                    } else {
                        userLotteryRecordUpdate.setWinAmount(DEFAULT_DISTRIBUTION_MIN_AMOUNT);
                    }
                }
            }
        }
    }

    private void distributeMachineRedPack(LotteryActivity lotteryActivity, DateTime now, List<UserLotteryRecord> joinUserLotteryRecordList, Integer userLotteryRecordState) {
        if (CollUtil.isEmpty(joinUserLotteryRecordList)) {
            log.warn("LotteryOpenDelayHandler-distributeMachineRedPack joinUserLotteryRecordList is empty lotteryActivity: {}", JSON.toJSONString(lotteryActivity));
            return;
        }
        for (UserLotteryRecord userLotteryRecordUpdate : joinUserLotteryRecordList) {
            if (Objects.isNull(userLotteryRecordUpdate.getWinType())) {
                userLotteryRecordUpdate.setUpdateTime(now);
                userLotteryRecordUpdate.setWinType(LotteryActivityTypeEnum.RED_PACK.getCode());
                userLotteryRecordUpdate.setState(UserLotteryRecordStateEnum.WIN.getCode());
                userLotteryRecordUpdate.setRobotFlag(true);
                userLotteryRecordUpdate.setWinTime(now);
            }
        }
    }
}
