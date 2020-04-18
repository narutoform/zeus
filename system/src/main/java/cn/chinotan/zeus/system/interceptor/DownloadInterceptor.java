package cn.chinotan.zeus.system.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 下载拦截器
 *
 * @author xingcheng
 * @date 2019/8/21
 */
@Slf4j
@ConditionalOnProperty(value = {"zeus.interceptor.download.enable"}, matchIfMissing = true)
public class DownloadInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果访问的不是控制器,则跳出,继续执行下一个拦截器
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 访问路径
        String url = request.getRequestURI();
        // 访问全路径
        String fullUrl = request.getRequestURL().toString();
        // 下载拦截器，业务处理代码
        log.debug("DownloadInterceptor...");
        // 访问token，如果需要，可以设置参数，进行鉴权
//        String token = request.getParameter(JwtTokenUtil.getTokenName());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 记录实际下载日志...
    }
}
