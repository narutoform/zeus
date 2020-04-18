package cn.chinotan.zeus.scheduled;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.delay.RedisDelayHandler;
import cn.chinotan.zeus.framework.common.service.BaseDelayTaskService;
import cn.chinotan.zeus.framework.common.vo.Task;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @program: lcarus
 * @description: 抽奖活动自动开奖
 * @author: xingcheng
 * @create: 2020-03-06 17:56
 **/
@Slf4j
@Component
@ConditionalOnProperty(name = "scheduled.delay.enable", havingValue = "true", matchIfMissing = false)
public class DelayRedisTask {

    @Autowired
    private RedisTemplate<String, Task> redisTaskTemplate;

    @Autowired(required = false)
    private Map<String, RedisDelayHandler> redisDelayHandlerMap;

    public static final Integer REDIS_SCAN_MAX = 100;

    @Autowired
    private BaseDelayTaskService baseDelayTaskService;

    /**
     * 每秒执行一次
     */
    @Scheduled(cron = "0/1 * * * * ? ")
    public void delayScan() {
        Set<ZSetOperations.TypedTuple<Task>> lotterySet = redisTaskTemplate.opsForZSet().rangeWithScores(CommonConstant.REDIS_DELAY, 0, REDIS_SCAN_MAX);
        if (CollUtil.isEmpty(lotterySet)) {
            return;
        }
        for (ZSetOperations.TypedTuple<Task> tuple : lotterySet) {
            Double score = tuple.getScore();
            long nowTime = System.currentTimeMillis() / 1000;
            if (Objects.nonNull(score) && nowTime >= score) {
                Task<?> task = tuple.getValue();
                Long result = redisTaskTemplate.opsForZSet().remove(CommonConstant.REDIS_DELAY, task);
                if (Objects.nonNull(result) && result > 0 && Objects.nonNull(task) && Objects.nonNull(redisDelayHandlerMap)) {
                    RedisDelayHandler redisDelayHandler = redisDelayHandlerMap.get(task.getType() + RedisDelayHandler.REDIS_DELAY_HANDLER);
                    if (Objects.nonNull(redisDelayHandler)) {
                        redisDelayHandler.delayExecute(task.getPayLoad());
                    }
                }
            }
        }
    }
}
