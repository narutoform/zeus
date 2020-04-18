package cn.chinotan.zeus.redislock.lock;

import cn.chinotan.zeus.redislock.Lock;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by ASUS on 2016/8/16.
 */
public class LockExpressionRootObject {

    private final Collection<? extends Lock> locks;

    private final Method method;

    private final Object[] args;

    private final Object target;

    private final Class<?> targetClass;

    public LockExpressionRootObject(Collection<? extends Lock> locks, Method method, Object[] args, Object target, Class<?> targetClass) {

        Assert.notNull(method, "Method is required");
        Assert.notNull(targetClass, "targetClass is required");
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.args = args;
        this.locks = locks;
    }

    public Collection<? extends Lock> getLocks() {
        return locks;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getTarget() {
        return target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
}
