package cn.chinotan.zeus.delay;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.config.constant.DelayType;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.lcarus.common.entity.LotteryActivity;
import cn.lcarus.common.entity.UserLotteryRecord;
import cn.lcarus.common.enums.LotteryActivityProductStateEnum;
import cn.lcarus.common.enums.LotteryActivityStateEnum;
import cn.lcarus.common.enums.UserLotteryRecordStateEnum;
import cn.lcarus.common.service.BaseLotteryActivityService;
import cn.lcarus.common.service.BaseUserLotteryRecordService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @program: lcarus
 * @description: 抽奖活动机器人自动参与定时任务
 * @author: xingcheng
 * @create: 2020-03-28 17:07
 **/
@Slf4j
@Service(DelayType.LOTTERY_ROBOT_JOIN + RedisDelayHandler.REDIS_DELAY_HANDLER)
public class LotteryRobotAutoJoinDelayHandler implements RedisDelayHandler<Long> {

    @Autowired
    private BaseLotteryActivityService baseLotteryActivityService;

    @Autowired
    private BaseUserLotteryRecordService baseUserLotteryRecordService;

    @Value("${lcarus.lottery.join.robot.enable:true}")
    private boolean enableJoinRobot;

    @Value("${lcarus.lottery.join.robot.robot-max-num}")
    private Integer robotMaxNum;

    @Value("${lcarus.lottery.join.robot.robot-min-num}")
    private Integer robotMinNum;

    @Override
    @Async
    @DistributeLock(value = "LOTTERY_ACTIVITY:ROBOT_AUTO_JOIN", key = "#lotteryActivityId", expire = 1024, timeout = 0, errMsg = "机器人自动参加处理中！")
    @Retryable(value = {Exception.class}, exclude = {IllegalStateException.class}, maxAttempts = 2, backoff = @Backoff(delay = 2000L, multiplier = 3))
    @Transactional(rollbackFor = Exception.class)
    public void delayExecute(Long lotteryActivityId) {
        if (!enableJoinRobot) {
            log.warn("LotteryRobotAutoJoinDelayHandler-delayExecute enableJoinRobot is false lotteryActivityId: {}", lotteryActivityId);
            return;
        }
        Asserts.notNull(lotteryActivityId, "lotteryActivityId");
        LotteryActivity lotteryActivity = baseLotteryActivityService.getLotteryActivityNewEst(lotteryActivityId);
        if (Objects.isNull(lotteryActivity)) {
            log.error("LotteryRobotAutoJoinDelayHandler-delayExecute lotteryActivity is null lotteryActivityId: {}", lotteryActivityId);
            return;
        }
        log.info("LotteryRobotAutoJoinDelayHandler-delayExecute lotteryActivity is: {}", JSON.toJSONString(lotteryActivity));
        // 校验活动状态，只处理已上架且待开奖的
        if (!Objects.equals(lotteryActivity.getState(), LotteryActivityStateEnum.WAIT_OPEN.getCode()) && !Objects.equals(lotteryActivity.getProductState(), LotteryActivityProductStateEnum.UP.getCode())) {
            log.error("LotteryOpenDelayHandler-delayExecute lotteryActivity state or productState is not wait_open or up lotteryActivityId: {}, state: {}", lotteryActivityId, lotteryActivity.getState());
            return;
        }

        // 增加随机机器人
        List<Long> defaultRobotUserIdSet = CommonConstant.DEFAULT_ROBOT_USER_ID;
        int size = defaultRobotUserIdSet.size();
        List<UserLotteryRecord> userLotteryRecordSaveList = new ArrayList<>(size);
        UserLotteryRecord userLotteryRecordSave;
        if (robotMaxNum > size) {
            robotMaxNum = size;
        }
        DateTime now = DateUtil.date();
        int randomInt = RandomUtil.randomInt(robotMinNum, robotMaxNum);
        Set<Long> robotUserIdSet = RandomUtil.randomEleSet(defaultRobotUserIdSet, randomInt);
        for (Long robotUserId : robotUserIdSet) {
            userLotteryRecordSave = new UserLotteryRecord();
            userLotteryRecordSave.setUserId(robotUserId);
            userLotteryRecordSave.setUpdateTime(now);
            userLotteryRecordSave.setCreateTime(now);
            userLotteryRecordSave.setLotteryActivityId(lotteryActivityId);
            userLotteryRecordSave.setState(UserLotteryRecordStateEnum.JOIN.getCode());
            userLotteryRecordSave.setRobotFlag(true);
            userLotteryRecordSaveList.add(userLotteryRecordSave);
        }
        // 保存机器人抽奖
        boolean saveUserLotteryRecord = baseUserLotteryRecordService.saveBatch(userLotteryRecordSaveList);
        Assert.isTrue(saveUserLotteryRecord);
    }
}
