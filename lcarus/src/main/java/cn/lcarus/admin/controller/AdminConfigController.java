package cn.lcarus.admin.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.admin.param.ConfigUpdateParam;
import cn.lcarus.common.service.BaseConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <pre>
 * 配置控制器
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
@Slf4j
@RestController
@RequestMapping("/config")
@Api("配置 API")
public class AdminConfigController extends BaseController {

    @Autowired
    private BaseConfigService baseConfigService;

    /**
     * 修改系统用户
     */
    @PostMapping("/v1/update")
    @ApiOperation(value = "修改配置对象", notes = "修改", response = ApiResult.class)
    public ApiResult<?> updateSysUser(@Valid @RequestBody ConfigUpdateParam param) {
        return baseConfigService.updateConfig(param);
    }

}

