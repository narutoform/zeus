package cn.chinotan.zeus.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置
 *
 * @author xingcheng
 * @date 2020/3/19
 */
@Configuration
public class AdminWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 设置项目静态资源访问
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

}
