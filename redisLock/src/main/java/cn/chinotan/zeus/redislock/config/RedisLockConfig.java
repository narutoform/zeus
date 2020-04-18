package cn.chinotan.zeus.redislock.config;

import cn.chinotan.zeus.redislock.RedisLockManager;
import cn.chinotan.zeus.redislock.lock.LockAspectSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * Redis Template 配置
 * </p>
 *
 * @author geekidea
 * @date 2018-11-08
 */
@Configuration
public class RedisLockConfig {

    /**
     * 分布式锁管理器
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnBean(RedisTemplate.class)
    public RedisLockManager getRedisLockManager(RedisTemplate redisTemplate) {
        RedisLockManager redisLockManager = new RedisLockManager();
        redisLockManager.setLockPrefix("LCARUS:REDIS:LOCK:");
        redisLockManager.setCacheLock(false);
        redisLockManager.setRedisTemplate(redisTemplate);
        return redisLockManager;
    }

    /**
     * 分布式锁
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnBean(RedisLockManager.class)
    public LockAspectSupport getLockAspectSupport(RedisTemplate redisTemplate) {
        LockAspectSupport lockAspectSupport = new LockAspectSupport();
        lockAspectSupport.setLockManager(getRedisLockManager(redisTemplate));
        return lockAspectSupport;
    }
}
