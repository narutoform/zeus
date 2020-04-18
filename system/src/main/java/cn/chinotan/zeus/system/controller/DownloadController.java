package cn.chinotan.zeus.system.controller;

import cn.chinotan.zeus.config.properties.ZeusProperties;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.log.annotation.Module;
import cn.chinotan.zeus.framework.log.annotation.OperationLog;
import cn.chinotan.zeus.framework.log.enums.OperationLogType;
import cn.chinotan.zeus.framework.util.DownloadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 下载控制器
 *
 * @author xingcheng
 * @date 2019/8/20
 * @since 1.2.1-RELEASE
 */
@Slf4j
@Controller
@RequestMapping("/download")
@Module("system")
@Api(value = "文件下载", tags = {"文件下载"})
public class DownloadController {

    @Autowired
    private ZeusProperties zeusProperties;

    /**
     * 下载文件
     */
    @GetMapping("/{downloadFileName}")
    @OperationLog(name = "下载文件", type = OperationLogType.download)
    @ApiOperation(value = "下载文件", notes = "下载文件", response = ApiResult.class)
    public void download(@PathVariable(required = true) String downloadFileName, HttpServletResponse response) throws Exception {
        // 下载目录，既是上传目录
        String downloadDir = zeusProperties.getUploadPath();
        // 允许下载的文件后缀
        List<String> allowFileExtensions = zeusProperties.getAllowDownloadFileExtensions();
        // 文件下载，使用默认下载处理器
        // 文件下载，使用自定义下载处理器
        DownloadUtil.download(downloadDir, downloadFileName, allowFileExtensions, response, (dir, fileName, file, fileExtension, contentType, length) -> {
            // 下载自定义处理，返回true：执行下载，false：取消下载
            log.info("dir = " + dir);
            log.info("fileName = " + fileName);
            log.info("file = " + file);
            log.info("fileExtension = " + fileExtension);
            log.info("contentType = " + contentType);
            log.info("length = " + length);
            return true;
        });
    }

}
