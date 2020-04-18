package cn.chinotan.zeus.redislock.annotation;

import java.lang.annotation.*;

/**
 * Created by karl on 2016/8/21.
 * Pessimistic Locking
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributeLocks {

    DistributeLock[] value();

}
