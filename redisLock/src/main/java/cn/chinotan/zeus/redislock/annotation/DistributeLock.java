package cn.chinotan.zeus.redislock.annotation;

import java.lang.annotation.*;

/**
 * Created by karl on 2016/8/21.
 * Pessimistic Locking
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributeLock {

    /**
     * The lock Name, if empty the value is `targetClass.getName() + "#" + method.getName()`.
     *
     * @return lock name
     */
    String value() default "";

    /**
     * Lock key (Spel), if empty keyGenerator will be work.
     */
    String key() default "";

    String errorCode() default "";

    /**
     * The error msg if get lock failed(new LockException(errMsg))
     * if empty will not throw LockException, just return false;
     *
     * @return
     */
    String errMsg() default "";

    /**
     * Timeout when getLock (seconds)
     *
     * @return getLock timeout in seconds
     */
    long timeout() default 20;

    /**
     * key expire time (seconds) if gt; 0
     *
     * @return key expire time in seconds
     */
    long expire() default 120;

    /**
     * auto unlock , if false please set expire greater 0
     *
     * @return true|false
     */
    boolean autoUnLock() default true;

}
