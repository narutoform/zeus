package cn.chinotan.zeus.redislock;

import cn.chinotan.zeus.redislock.exception.LockException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ASUS on 2016/8/16.
 */
public class RedisLock implements Lock {
    private static Log log = LogFactory.getLog(RedisLock.class);

    private RedisTemplate<Object, Object> redisTemplate;

    private final String name;
    private final String key;
    private final long timeout;//ms
    private final long expire;//ms
    private final String code;
    private final String msg;
    private final boolean autoUnlock;

    private final String lockName;

    public RedisLock(String name, String key, long timeout, long expire, String code, String msg, RedisTemplate redisTemplate, boolean autoUnlock) {
        this.name = name;
        this.key = key;
        this.timeout = timeout;
        this.expire = expire;
        this.lockName = this.name + ":" + key;
        this.redisTemplate = redisTemplate;
        this.code = code;
        this.msg = msg;
        this.autoUnlock = autoUnlock;
    }

    @Override
    public boolean autoUnlock() {
        return autoUnlock;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public long getTimeout() {
        return this.timeout;
    }

    @Override
    public long getKeyExpire() {
        return this.expire;
    }

    @Override
    public String getLockName() {
        return this.lockName;
    }

    private final ReentrantLock takeLock = new ReentrantLock();
    private final Condition lockCondition = takeLock.newCondition();

    @Override
    public synchronized boolean lock() throws LockException {
        this.locked = false;
        if (StringUtils.isEmpty(name)) {
            throw new LockException("lock name is null");
        }
        this.tryLock();
        //has get the lock
        if (locked) {
            log.debug(this + " Get Lock: " + this.lockName);
            return true;
        }
        if (!msg.isEmpty()) {
            throw new LockException(code, msg);
        }
        return false;
    }

    private volatile boolean cancel = false;

    private volatile boolean locked = false;
    long MAX_TIMEOUT = 1000 * 3600;

    private void tryLock() {
        takeLock.lock();
        try {
            BoundValueOperations operations = redisTemplate.boundValueOps(lockName);
            long st = System.currentTimeMillis();
            while (!locked) {
                if (cancel) {
                    break;
                }
                long timestamp = System.currentTimeMillis();
                if (timeout > 0 && timestamp - st > timeout) {
                    break;
                }

                long expireTime = (expire < 1) ? Long.MAX_VALUE : (timestamp + this.expire);
                long oldValue = get(operations);
                if (oldValue > 0 && timestamp > oldValue) {
                    String newName = "DEL:" + lockName;
                    try {
                        Boolean renameSuccess = redisTemplate.renameIfAbsent(lockName, newName);//cas op to ensure only one thread success;
                        if (Boolean.TRUE.equals(renameSuccess)) {
                            redisTemplate.delete(newName);
                        }
                    } catch (InvalidDataAccessApiUsageException e) {//ERR no such key
                    }
                }

                locked = operations.setIfAbsent(String.valueOf(expireTime));
                if (locked) {
                    operations.expire(expire, TimeUnit.MILLISECONDS);
                    log.debug("Locked " + operations.getKey());
                    return;
                } else {
                    if (timeout == 0) {
                        break;
                    }
                    try {
                        if (timeout > 10 * 1000) {
                            lockCondition.await(timeout / 10, TimeUnit.MILLISECONDS);
                        } else {
                            lockCondition.await(timeout, TimeUnit.MILLISECONDS);
                        }
                    } catch (InterruptedException e) {
                    }
                    if (timeout > 0 && timestamp - st > timeout) {
                        break;
                    }
                    if (timestamp - st > MAX_TIMEOUT) {
                        break;
                    }
                }
            }
        } finally {
            lockCondition.signalAll();
            takeLock.unlock();
        }
    }

    @Override
    public void unlock() {
        if (locked) {
            redisTemplate.delete(lockName);
            locked = false;
            cancel = false;
        }
    }

    private long get(BoundValueOperations valueOperations) {
        Object value = valueOperations.get();
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            return StringUtils.isEmpty(value) ? 0 : Long.parseLong(value.toString());
        }
        return 0;
    }

}
