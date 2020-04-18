package cn.chinotan.zeus.system.controller;

import cn.chinotan.zeus.config.properties.ZeusProperties;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.log.annotation.Module;
import cn.chinotan.zeus.framework.log.annotation.OperationLog;
import cn.chinotan.zeus.framework.log.enums.OperationLogType;
import cn.chinotan.zeus.framework.util.UploadUtil;
import cn.chinotan.zeus.system.service.OssService;
import cn.chinotan.zeus.system.vo.OssPathvo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 上传控制器
 *
 * @author xingcheng
 * @date 2019/8/20
 * @since 1.2.1-RELEASE
 */
@Slf4j
@RestController
@RequestMapping("/upload")
@Module("system")
@Api(value = "文件上传", tags = {"文件上传"})
public class UploadController {

    @Autowired
    private ZeusProperties zeusProperties;

    @Autowired
    private OssService ossService;

    private static final String LOTTERY_ACTIVITY_IMAGE = "delay/activity/image/";

    /**
     * 上传单个文件
     * @return
     */
    @PostMapping
    @OperationLog(name = "上传单个文件", type = OperationLogType.UPLOAD)
    @ApiOperation(value = "上传单个文件", response = ApiResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true,dataType = "__file"),
            @ApiImplicitParam(name = "type", value = "类型 head:头像",required = true)
    })
    public ApiResult<String> upload(@RequestParam("file") MultipartFile multipartFile,
                                    @RequestParam("type") String type) throws Exception {
        uploadLogPrint(multipartFile);

        // 上传文件，返回保存的文件名称
        String saveFileName = UploadUtil.upload(zeusProperties.getUploadPath(), multipartFile, originalFilename -> {
            // 文件后缀
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            // 这里可自定义文件名称，比如按照业务类型/文件格式/日期
            String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssS")) + RandomStringUtils.randomNumeric(6);
            String fileName = dateString + "." + fileExtension;
            return fileName;
        });

        // 上传成功之后，返回访问路径，请根据实际情况设置

        String fileAccessPath = zeusProperties.getResourceAccessUrl() + saveFileName;
        log.info("fileAccessPath:{}", fileAccessPath);

        return ApiResult.ok(fileAccessPath);
    }

    @PostMapping("/v1/aliYunOss")
    @ApiOperation(value = "上传单个文件到阿里云OSS", notes = "上传单个文件",response = ApiResult.class)
    public ApiResult<Boolean> uploadAliYunOss(@RequestParam("img") MultipartFile multipartFile) throws Exception{
        uploadLogPrint(multipartFile);

        // 上传文件，返回保存的文件名称
        OssPathvo ossPathVo = ossService.upload(multipartFile, LOTTERY_ACTIVITY_IMAGE);
        return ApiResult.ok(ossPathVo);
    }

    private void uploadLogPrint(MultipartFile multipartFile) {
        log.info("multipartFile = " + multipartFile);
        log.info("ContentType = " + multipartFile.getContentType());
        log.info("OriginalFilename = " + multipartFile.getOriginalFilename());
        log.info("Name = " + multipartFile.getName());
        log.info("Size = " + multipartFile.getSize());
    }
}
