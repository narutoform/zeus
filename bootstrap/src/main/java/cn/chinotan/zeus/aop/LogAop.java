package cn.chinotan.zeus.aop;

import cn.chinotan.zeus.framework.log.aop.BaseLogAop;
import cn.chinotan.zeus.framework.log.bean.OperationLogInfo;
import cn.chinotan.zeus.framework.log.bean.RequestInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Controller Aop
 * 获取响应结果信息
 * </p>
 *
 * @author xingcheng
 * @date 2019-10-23
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(value = {"zeus.aop.log.enable"}, matchIfMissing = true)
public class LogAop extends BaseLogAop {

    /**
     * 切点
     */
    private static final String POINTCUT =
            "execution(public * cn.chinotan.zeus..*.controller..*.*(..)) || " +
                    "execution(public * cn.lcarus.admin.controller..*.*(..))";

    @Around(POINTCUT)
    @Override
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.handle(joinPoint);
    }

    @AfterThrowing(pointcut = POINTCUT, throwing = "exception")
    @Override
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        super.handleAfterThrowing(exception);
    }

    @Override
    protected void setRequestId(RequestInfo requestInfo) {
        super.handleRequestId(requestInfo);
    }

    @Override
    protected void getRequestInfo(RequestInfo requestInfo) {
        // 处理请求参数日志
        super.handleRequestInfo(requestInfo);
    }

    @Override
    protected void getResponseResult(Object result) {
        // 处理响应结果日志
        super.handleResponseResult(result);
    }

    @Override
    protected void finish(RequestInfo requestInfo, OperationLogInfo operationLogInfo, Object result, Exception exception) {
        // 异步保存操作日志
        super.saveSysOperationLog(requestInfo, operationLogInfo, result, exception);
        // 异步保存登录日志
        super.saveSysLoginLog(requestInfo, operationLogInfo, result, exception);
    }

}
