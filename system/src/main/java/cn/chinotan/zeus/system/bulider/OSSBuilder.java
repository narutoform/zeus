package cn.chinotan.zeus.system.bulider;

/**
 * @program: guess
 * @description:
 * @author: xingcheng
 * @create: 2019-09-21 12:10
 **/

import com.aliyun.oss.OSS;

/**
 * Fluent builder for OSS Client. Use of the builder is preferred over using
 * constructors of the client class.
 */
public interface OSSBuilder {

    /**
     * Uses the specified OSS Endpoint and Access Id/Access Key to create a new
     *
     * @param endpoint        OSS endpoint.
     * @param accessKeyId     Access Key ID.
     * @param secretAccessKey Secret Access Key.
     */
    OSS build(String endpoint, String accessKeyId, String secretAccessKey);

}
