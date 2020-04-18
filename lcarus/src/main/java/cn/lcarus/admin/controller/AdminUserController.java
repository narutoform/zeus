package cn.lcarus.admin.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.admin.param.UserQueryParam;
import cn.lcarus.admin.res.UserInfoVo;
import cn.lcarus.common.service.BaseUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author xincheng
 * @since 2019-10-24
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api("用户相关接口 API")
public class AdminUserController extends BaseController {

    @Autowired
    private BaseUserService baseUserService;

    @GetMapping("/v1/list")
    @ApiOperation(value = "列表展示", notes = "接口")
    public ApiResult<Page<UserInfoVo>> showList(@Valid UserQueryParam param) {
        return baseUserService.showList(param);
    }

}

