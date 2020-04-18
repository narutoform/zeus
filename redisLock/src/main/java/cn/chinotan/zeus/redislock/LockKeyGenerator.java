package cn.chinotan.zeus.redislock;

import java.lang.reflect.Method;

/**
 * Created by ASUS on 2016/8/16.
 */
public interface LockKeyGenerator {

    /**
     * Generate a key for the given method and its parameters.
     *
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return a generated key
     */
    Object generate(Object target, Method method, Object... params);

}
