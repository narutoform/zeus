package cn.chinotan.zeus.delay;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

/**
 * @program: lcarus
 * @description: 抽奖活动定时开奖
 * @author: xingcheng
 * @create: 2020-03-06 21:06
 **/
public interface RedisDelayHandler<T> {
    
    String REDIS_DELAY_HANDLER = ":RedisDelayHandler";

    /**
     * 延时任务处理
     * @param payload
     */
    @Async
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 3))
    void delayExecute(T payload);
    
}
