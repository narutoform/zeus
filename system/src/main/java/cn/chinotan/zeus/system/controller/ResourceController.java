package cn.chinotan.zeus.system.controller;

import cn.chinotan.zeus.config.properties.ZeusProperties;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.log.annotation.Module;
import cn.chinotan.zeus.framework.log.annotation.OperationLog;
import cn.chinotan.zeus.framework.log.enums.OperationLogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * 图片等文件资源访问控制器
 * /api/resource 访问路径 用于区分 文件访问虚拟目录映射 /resource
 *
 * @author xingcheng
 * @date 2019/8/20
 * @since 1.2.1-RELEASE
 */
@Slf4j
@Controller
@RequestMapping("/api/resource")
@Module("system")
@Api(value = "资源访问", tags = {"资源访问"})
public class ResourceController {

    @Autowired
    private ZeusProperties zeusProperties;

    /**
     * 访问资源
     */
    @GetMapping("/image/{imageFileName}")
    @OperationLog(name = "访问资源", type = OperationLogType.ADD)
    @ApiOperation(value = "访问资源", response = ApiResult.class)
    public void getImage(@PathVariable(required = true) String imageFileName, HttpServletResponse response) throws Exception {
        log.info("imageFileName:{}", imageFileName);
        // 重定向到图片访问路径
        response.sendRedirect(zeusProperties.getResourceAccessPath() + imageFileName);
    }

}
