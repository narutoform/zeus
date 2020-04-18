package cn.chinotan.zeus.system.service;

import cn.chinotan.zeus.system.vo.OssPathvo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 用户三方信息 服务类
 *
 * @author xingcheng
 * @since 2020-03-05
 */
public interface OssService {

    /**
     * 上传文件
     *
     * @param file
     * @param pathPrefix
     */
    OssPathvo upload(File file, String pathPrefix);

    /**
     * 上传文件
     *
     * @param multipartFile
     * @param pathPrefix
     */
    OssPathvo upload(MultipartFile multipartFile, String pathPrefix);

}
