package cn.chinotan.zeus.framework.front.filter;

import cn.chinotan.zeus.framework.common.vo.CacheRequestWrapper;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: guess
 * @description: 请求过滤器
 * @author: xingcheng
 * @create: 2019-08-31 20:47
 **/
@Order
public class RequestBodyFilter implements Filter {

    /**
     * 防止body流使用后关闭
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        ServletRequest requestWrapper = new CacheRequestWrapper(req);
        filterChain.doFilter(requestWrapper, servletResponse);
    }

}
