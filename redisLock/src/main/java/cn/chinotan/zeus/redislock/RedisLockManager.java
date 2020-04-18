package cn.chinotan.zeus.redislock;

import cn.chinotan.zeus.redislock.lock.LockOperation;
import cn.chinotan.zeus.redislock.lock.LockOperationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 2016/8/16.
 */
public class RedisLockManager implements LockManager {

    private RedisTemplate<Object, Object> redisTemplate;

    private Map<String, RedisLock> _localCache = new HashMap<String, RedisLock>();

    private boolean cacheLock = false;

    private String lockPrefix = "Lock_";

    @Override
    public Lock getLock(LockOperationContext context, String key) {
        LockOperation operation = context.getMetadata().getOperation();
        String name = (lockPrefix != null ? lockPrefix : "") + operation.getName();
        long timeout = operation.getTimeout() * 1000;
        long expire = operation.getExpire() * 1000;
        if (!cacheLock) {
            return new RedisLock(name, key, timeout, expire, operation.getCode(), operation.getMsg(), redisTemplate, operation.isAutoUnlock());
        }

        RedisLock lock = _localCache.get(name + key);
        if (lock == null) {
            lock = new RedisLock(name, key, timeout, expire, operation.getCode(), operation.getMsg(), redisTemplate, operation.isAutoUnlock());
            _localCache.put(name + key, lock);
        }
        return lock;
    }

    public String getLockPrefix() {
        return lockPrefix;
    }

    public void setLockPrefix(String lockPrefix) {
        this.lockPrefix = lockPrefix;
    }

    public boolean isCacheLock() {
        return cacheLock;
    }

    public void setCacheLock(boolean cacheLock) {
        this.cacheLock = cacheLock;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
