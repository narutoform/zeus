package cn.chinotan.zeus.system.config;

import com.aliyun.oss.ClientConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: guess
 * @description:
 * @author: xingcheng
 * @create: 2019-09-21 12:15
 **/
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class AliYunOssClientBuilderConfig extends ClientConfiguration {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private String rootUrl;

    public AliYunOssClientBuilderConfig() {
        super();
        setSupportCname(false);
    }
}
