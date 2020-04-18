package cn.chinotan.zeus.system.bulider.impl;

import cn.chinotan.zeus.system.bulider.OSSBuilder;
import cn.chinotan.zeus.system.config.AliYunOssClientBuilderConfig;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;

/**
 * Fluent builder for OSS Client. Use of the builder is preferred over using
 * constructors of the client class.
 */
public class OSSClientBuilder implements OSSBuilder {

    @Override
    public OSS build(String endpoint, String accessKeyId, String secretAccessKey) {
        return new OSSClient(endpoint, getDefaultCredentialProvider(accessKeyId, secretAccessKey),
                getClientConfiguration());
    }

    private static AliYunOssClientBuilderConfig getClientConfiguration() {
        return new AliYunOssClientBuilderConfig();
    }

    private static AliYunOssClientBuilderConfig getClientConfiguration(AliYunOssClientBuilderConfig config) {
        if (config == null) {
            config = new AliYunOssClientBuilderConfig();
        }
        return config;
    }

    private static DefaultCredentialProvider getDefaultCredentialProvider(String accessKeyId, String secretAccessKey) {
        return new DefaultCredentialProvider(accessKeyId, secretAccessKey);
    }

    private static DefaultCredentialProvider getDefaultCredentialProvider(String accessKeyId, String secretAccessKey,
                                                                          String securityToken) {
        return new DefaultCredentialProvider(accessKeyId, secretAccessKey, securityToken);
    }

}
