package cn.chinotan.zeus.redislock;


import cn.chinotan.zeus.redislock.lock.LockOperationContext;

/**
 * Created by ASUS on 2016/8/16.
 */
public interface LockManager {

    Lock getLock(LockOperationContext context, String key);

}
