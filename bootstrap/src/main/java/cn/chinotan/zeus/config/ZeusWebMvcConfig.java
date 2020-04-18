package cn.chinotan.zeus.config;

import cn.chinotan.zeus.framework.front.intercepter.JwtInterceptor;
import cn.chinotan.zeus.framework.front.intercepter.TokenToUserIdInterceptor;
import com.alibaba.fastjson.JSON;
import cn.chinotan.zeus.config.properties.ZeusFilterProperties;
import cn.chinotan.zeus.config.properties.ZeusInterceptorProperties;
import cn.chinotan.zeus.config.properties.ZeusProperties;
import cn.chinotan.zeus.framework.core.filter.RequestDetailFilter;
import cn.chinotan.zeus.framework.core.interceptor.PermissionInterceptor;
import cn.chinotan.zeus.framework.core.xss.XssFilter;
import cn.chinotan.zeus.framework.util.IniUtil;
import cn.chinotan.zeus.system.interceptor.DownloadInterceptor;
import cn.chinotan.zeus.system.interceptor.ResourceInterceptor;
import cn.chinotan.zeus.system.interceptor.UploadInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * WebMvc配置
 *
 * @author xingcheng
 * @date 2018-11-08
 */
@Slf4j
@Configuration
public class ZeusWebMvcConfig implements WebMvcConfigurer {

    /**
     * zeus配置属性
     */
    @Autowired
    private ZeusProperties zeusProperties;

    /**
     * Filter配置
     */
    private ZeusFilterProperties filterConfig;

    /**
     * 拦截器配置
     */
    private ZeusInterceptorProperties interceptorConfig;

    /**
     * RequestDetailFilter配置
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean requestDetailFilter() {
        ZeusFilterProperties.FilterConfig requestFilterConfig = filterConfig.getRequest();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new RequestDetailFilter());
        filterRegistrationBean.setEnabled(requestFilterConfig.isEnable());
        filterRegistrationBean.addUrlPatterns(requestFilterConfig.getUrlPatterns());
        filterRegistrationBean.setOrder(requestFilterConfig.getOrder());
        filterRegistrationBean.setAsyncSupported(requestFilterConfig.isAsync());
        return filterRegistrationBean;
    }

    /**
     * XssFilter配置
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean xssFilter() {
        ZeusFilterProperties.FilterConfig xssFilterConfig = filterConfig.getXss();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setEnabled(xssFilterConfig.isEnable());
        filterRegistrationBean.addUrlPatterns(xssFilterConfig.getUrlPatterns());
        filterRegistrationBean.setOrder(xssFilterConfig.getOrder());
        filterRegistrationBean.setAsyncSupported(xssFilterConfig.isAsync());
        return filterRegistrationBean;
    }


    /**
     * 自定义权限拦截器
     *
     * @return
     */
    @Bean
    public PermissionInterceptor permissionInterceptor() {
        return new PermissionInterceptor();
    }

    /**
     * 上传拦截器
     *
     * @return
     */
    @Bean
    public UploadInterceptor uploadInterceptor() {
        return new UploadInterceptor();
    }

    /**
     * 资源拦截器
     *
     * @return
     */
    @Bean
    public ResourceInterceptor resourceInterceptor() {
        return new ResourceInterceptor();
    }

    /**
     * 下载拦截器
     *
     * @return
     */
    @Bean
    public DownloadInterceptor downloadInterceptor() {
        return new DownloadInterceptor();
    }

    /**
     * JWT TOKEN验证拦截器
     * @return
     */
    @Bean
    public JwtInterceptor jwtInterceptor(){
        JwtInterceptor jwtInterceptor = new JwtInterceptor();
        return jwtInterceptor;
    }

    /**
     * token转userId拦截器
     * @return
     */
    @Bean
    public TokenToUserIdInterceptor tokenToUserIdInterceptor(){
        TokenToUserIdInterceptor tokenToUserIdInterceptor = new TokenToUserIdInterceptor();
        return tokenToUserIdInterceptor;
    }

    @PostConstruct
    public void init() {
        filterConfig = zeusProperties.getFilter();
        interceptorConfig = zeusProperties.getInterceptor();
        // 打印ZeusProperties配置信息
        log.debug("ZeusProperties：{}", JSON.toJSONString(zeusProperties));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 上传拦截器
        if (interceptorConfig.getUpload().isEnable()) {
            registry.addInterceptor(uploadInterceptor())
                    .addPathPatterns(interceptorConfig.getUpload().getIncludePaths());
        }

        // 资源拦截器注册
        if (interceptorConfig.getResource().isEnable()) {
            registry.addInterceptor(resourceInterceptor())
                    .addPathPatterns(interceptorConfig.getResource().getIncludePaths());
        }

        // 下载拦截器注册
        if (interceptorConfig.getDownload().isEnable()) {
            registry.addInterceptor(downloadInterceptor())
                    .addPathPatterns(interceptorConfig.getDownload().getIncludePaths());
        }

        // 自定义权限拦截器注册
        if (interceptorConfig.getPermission().isEnable()) {
            registry.addInterceptor(permissionInterceptor())
                    .addPathPatterns(interceptorConfig.getPermission().getIncludePaths())
                    .excludePathPatterns(interceptorConfig.getPermission().getExcludePaths());
        }

        // front 拦截器
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns(interceptorConfig.getFrontJwt().getIncludePaths())
                .excludePathPatterns(interceptorConfig.getFrontJwt().getExcludePaths());

        // token转userId拦截器
        registry.addInterceptor(tokenToUserIdInterceptor())
                .addPathPatterns(interceptorConfig.getFrontTokenToUserId().getIncludePaths())
                .excludePathPatterns(interceptorConfig.getFrontTokenToUserId().getExcludePaths());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 设置项目静态资源访问
        String resourceHandlers = zeusProperties.getResourceHandlers();
        if (StringUtils.isNotBlank(resourceHandlers)) {
            Map<String, String> map = IniUtil.parseIni(resourceHandlers);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String pathPatterns = entry.getKey();
                String resourceLocations = entry.getValue();
                registry.addResourceHandler(pathPatterns)
                        .addResourceLocations(resourceLocations);
            }
        }

        // 设置上传文件访问路径
        registry.addResourceHandler(zeusProperties.getResourceAccessPatterns())
                .addResourceLocations("file:" + zeusProperties.getUploadPath());
    }

}
