package cn.chinotan.zeus.system.service.impl;

import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.exception.BusinessException;
import cn.chinotan.zeus.framework.util.UUIDUtil;
import cn.chinotan.zeus.system.bulider.impl.OSSClientBuilder;
import cn.chinotan.zeus.system.config.AliYunOssClientBuilderConfig;
import cn.chinotan.zeus.system.service.OssService;
import cn.chinotan.zeus.system.vo.OssPathvo;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 用户三方信息 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-05
 */
@Service
@Slf4j
public class AliYunOssServiceImpl implements OssService {

    @Autowired
    private AliYunOssClientBuilderConfig aliYunOssClientBuilderConfig;

    @Override
    public OssPathvo upload(File file, String pathPrefix) {
        Assert.notNull(file);
        return uploadFile(file, pathPrefix);
    }

    @Override
    public OssPathvo upload(MultipartFile multipartFile, String pathPrefix) {
        Assert.notNull(pathPrefix);
        return uploadFile(multipartFile, pathPrefix);
    }

    private OssPathvo uploadFile(MultipartFile multipartFile, String pathPrefix) {
        Assert.notNull(multipartFile, pathPrefix);
        OSS client = null;
        try {
            // 获取文件名
            String fileName = StrUtil.trim(multipartFile.getOriginalFilename());
            List<String> list = StrUtil.splitTrim(fileName, StrUtil.DOT);
            if (list.size() != 2) {
                log.error("fileName split invalid {}", fileName);
                throw new BusinessException(ApiCode.CODE_100200);
            }
            String fileFormat = list.get(1);
            client = new OSSClientBuilder().build(aliYunOssClientBuilderConfig.getEndpoint(), aliYunOssClientBuilderConfig.getAccessKeyId(), aliYunOssClientBuilderConfig.getAccessKeySecret());
            String filePath = StringUtils.join(pathPrefix, UUIDUtil.getUuid(), StrUtil.DOT, fileFormat);
            try {
                PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(aliYunOssClientBuilderConfig.getBucketName(), filePath, multipartFile.getInputStream()));
            } catch (IOException e) {
                log.error("OssService-multipartFile.getInputStream IOException", e);
                throw new BusinessException(e);
            }
            String absoluteAddress = aliYunOssClientBuilderConfig.getRootUrl() + filePath;
            log.info("OssService-uploadImage success fileName: {}, filePath: {}, absoluteAddress: {}", fileName, filePath, absoluteAddress);
            return new OssPathvo(StrUtil.SLASH + filePath, absoluteAddress);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message: " + oe.getErrorCode());
            log.error("Error Code:       " + oe.getErrorCode());
            log.error("Request ID:      " + oe.getRequestId());
            log.error("Host ID:           " + oe.getHostId());
            throw new BusinessException(oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message: " + ce.getMessage());
            throw new BusinessException(ce);
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }

    private OssPathvo uploadFile(File file, String pathPrefix) {
        Assert.notNull(file, pathPrefix);
        OSS client = null;
        try {
            // 获取文件名
            String fileName = FileUtil.getName(file).trim();
            List<String> list = StrUtil.splitTrim(fileName, StrUtil.DOT);
            if (list.size() != 2) {
                log.error("fileName split invalid {}", fileName);
                throw new BusinessException(ApiCode.CODE_100200);
            }
            String fileFormat = list.get(1);
            client = new OSSClientBuilder().build(aliYunOssClientBuilderConfig.getEndpoint(), aliYunOssClientBuilderConfig.getAccessKeyId(), aliYunOssClientBuilderConfig.getAccessKeySecret());
            String filePath = StringUtils.join(pathPrefix, UUIDUtil.getUuid(), StrUtil.DOT, fileFormat);
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(aliYunOssClientBuilderConfig.getBucketName(), filePath, file));
            String absoluteAddress = aliYunOssClientBuilderConfig.getRootUrl() + filePath;
            log.info("OssService-uploadImage success fileName: {}, filePath: {}, absoluteAddress: {}", fileName, filePath, absoluteAddress);
            return new OssPathvo(StrUtil.SLASH + filePath, absoluteAddress);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message: " + oe.getErrorCode());
            log.error("Error Code:       " + oe.getErrorCode());
            log.error("Request ID:      " + oe.getRequestId());
            log.error("Host ID:           " + oe.getHostId());
            throw new BusinessException(oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message: " + ce.getMessage());
            throw new BusinessException(ce);
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }
}
