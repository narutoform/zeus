package cn.chinotan.zeus.redislock.lock;


import cn.chinotan.zeus.redislock.LockKeyGenerator;

import java.lang.reflect.Method;

/**
 * Created by ASUS on 2016/8/16.
 */
public class ParameterLockKeyGenerator implements LockKeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return generateKey(params);
    }

    /**
     * Generate a key based on the specified parameters.
     */
    public static Object generateKey(Object... params) {
        if (params == null || params.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (Object param : params) {
            if (buffer.length() > 0) {
                buffer.append("_");
            }
            buffer.append(String.valueOf(param));
        }
        return buffer.toString();
    }

}
