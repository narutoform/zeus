package cn.chinotan.zeus.redislock.lock;

import cn.chinotan.zeus.redislock.Lock;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ASUS on 2016/8/16.
 */
public class LockOperationContext {

    private final LockOperationMetadata metadata;
    private final Object[] args;
    private final Object target;
    private final Method method;
    private final AnnotatedElementKey methodCacheKey;

    public LockOperationContext(LockOperationMetadata metadata, Object target, Method method, Object[] args) {
        this.metadata = metadata;
        this.args = extractArgs(metadata.getMethod(), args);
        this.target = target;
        this.method = method;
        this.methodCacheKey = new AnnotatedElementKey(metadata.getMethod(), metadata.getTargetClass());
    }

    private Object[] extractArgs(Method method, Object[] args) {
        if (!method.isVarArgs()) {
            return args;
        }
        Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
        Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
        System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
        System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
        return combinedArgs;
    }

    private Collection<String> parseLockNames(Collection<? extends LockOperation> locks) {
        if (locks == null) {
            return null;
        }
        Collection<String> names = new ArrayList<String>(locks.size());
        for (LockOperation lockOperation : locks) {
            names.add(lockOperation.getName());
        }
        return names;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getTarget() {
        return target;
    }

    public AnnotatedElementKey getMethodCacheKey() {
        return methodCacheKey;
    }

    public LockOperationMetadata getMetadata() {
        return metadata;
    }

    public Method getMethod() {
        return method;
    }

    protected Object generateLockKey(LockExpressionEvaluator evaluator) {
        if (StringUtils.hasText(this.metadata.getOperation().getKey())) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(new ArrayList<Lock>(), this.target,
                    metadata.getTargetClass(), metadata.getMethod(), this.args);
            return evaluator.key(this.metadata.getOperation().getKey(), this.methodCacheKey, evaluationContext);
        }
        return this.metadata.getKeyGenerator().generate(this.target, this.metadata.getMethod(), this.args);
    }

}
