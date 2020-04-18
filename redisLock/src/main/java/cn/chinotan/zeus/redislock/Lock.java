package cn.chinotan.zeus.redislock;

import cn.chinotan.zeus.redislock.exception.LockException;

/**
 * Created by ASUS on 2016/8/16.
 * Pessimistic Locking
 * 悲观锁，依赖于分布式
 */
public interface Lock {

    String getName();

    String getKey();

    long getTimeout();

    long getKeyExpire();

    String getLockName();

    boolean lock() throws LockException;

    void unlock();

    boolean autoUnlock();

}
