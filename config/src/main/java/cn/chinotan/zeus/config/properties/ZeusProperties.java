package cn.chinotan.zeus.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * zeus属性配置信息
 *
 * @author xingcheng
 * @date 2019-08-04
 */
@Data
@Component
@ConfigurationProperties(prefix = "zeus")
public class ZeusProperties {

    /**
     * 是否启用ansi控制台输出有颜色的字体，local环境建议开启，服务器环境设置为false
     */
    private boolean enableAnsi;

    /**
     * 项目IP或域名地址
     */
    private String serverIp;

    /**
     * 是否启用验证码
     */
    private boolean enableVerifyCode;

    /**
     * 新建登录用户初始化盐值
     */
    private String loginInitSalt;

    /**
     * 新建登录用户初始化密码
     */
    private String loginInitPassword;

    /**
     * 新建用户初始化头像
     */
    private String loginInitHead;

    /**
     * 实现BaseEnum接口的枚举包
     */
    private String[] enumPackages;

    /**
     * 拦截器配置
     */
    @NestedConfigurationProperty
    private ZeusInterceptorProperties interceptor;

    /**
     * 过滤器配置
     */
    @NestedConfigurationProperty
    private ZeusFilterProperties filter;

    /**
     * 上传目录
     */
    private String uploadPath;
    /**
     * 资源访问路径，前端访问
     */
    private String resourceAccessPath;
    /**
     * 资源访问路径，后段配置，资源映射/拦截器使用
     */
    private String resourceAccessPatterns;
    /**
     * 资源访问全路径
     */
    private String resourceAccessUrl;

    /**
     * 允许上传的文件后缀集合
     */
    private List<String> allowUploadFileExtensions;
    /**
     * 允许下载的文件后缀集合
     */
    private List<String> allowDownloadFileExtensions;

    /**
     * JWT配置
     */
    @NestedConfigurationProperty
    private JwtProperties jwt;

    /**
     * Shiro配置
     */
    @NestedConfigurationProperty
    private ShiroProperties shiro = new ShiroProperties();

    /**
     * 项目静态资源访问配置
     *
     * ZeusWebMvcConfig addResourceHandlers
     */
    private String resourceHandlers;

    /**
     * 跨域配置
     */
    @NestedConfigurationProperty
    private ZeusCorsProperties cors = new ZeusCorsProperties();

    /**
     * Swagger路径
     */
    private List<String> swaggerPaths;

}
