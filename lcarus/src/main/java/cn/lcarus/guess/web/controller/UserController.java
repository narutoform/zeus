package cn.lcarus.guess.web.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.common.service.BaseUserService;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserAddMobileParam;
import cn.lcarus.guess.web.res.UserSelfCenterInfoRes;
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
 * @program: guess
 * @description: 用户相关接口
 * @author: xingcheng
 * @create: 2020-03-02 21:12
 **/
@Slf4j
@RestController
@RequestMapping("/api/user")
@Api(value = "用户相关接口 API", description = "02.用户相关接口")
public class UserController extends BaseController {

    @Autowired
    private BaseUserService userService;

    @PostMapping("/v1/info")
    @ApiOperation(value = "用户信息请求接口")
    public ApiResult<UserSelfCenterInfoRes> showInfo(@Valid @RequestBody LoginUserVo param) {
        return userService.showSelfCenterInfo(param);
    }

    @PostMapping("/v1/addMobile")
    @ApiOperation(value = "用户添加手机号请求接口")
    public ApiResult<?> addMobile(@Valid @RequestBody UserAddMobileParam param) {
        return userService.addMobile(param);
    }
}
