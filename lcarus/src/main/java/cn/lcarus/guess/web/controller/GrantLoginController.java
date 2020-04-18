package cn.lcarus.guess.web.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.common.service.BaseUserThirdInfoService;
import cn.lcarus.guess.web.param.ThirdUserDetailSyncParam;
import cn.lcarus.guess.web.param.UserGrantLoginParam;
import cn.lcarus.guess.web.param.UserSyncThirdSessionParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: guess
 * @description: 授权登录请求
 * @author: xingcheng
 * @create: 2019-08-31 21:12
 **/
@Slf4j
@RestController
@RequestMapping("/api/grant")
@Api(value = "授权登录请求 API", description = "01.授权登录相关接口")
public class GrantLoginController extends BaseController {

    @Autowired
    private BaseUserThirdInfoService baseUserThirdInfoService;
    
    @PostMapping("/v1/login")
    @ApiOperation(value = "授权登录请求接口")
    public ApiResult<String> showLottery(@Valid @RequestBody UserGrantLoginParam param) throws WxErrorException {
        return baseUserThirdInfoService.grantLogin(param);
    }

    @PostMapping("/v1/syncThirdInfo")
    @ApiOperation(value = "同步资料接口")
    public ApiResult<?> syncThirdInfo(@Valid @RequestBody ThirdUserDetailSyncParam param) throws WxErrorException {
        return baseUserThirdInfoService.syncThirdInfo(param);
    }

    @PostMapping("/v1/syncThirdSession")
    @ApiOperation(value = "登录同步三方session接口")
    public ApiResult<?> loginSyncSession(@Valid @RequestBody UserSyncThirdSessionParam param) throws WxErrorException {
        return baseUserThirdInfoService.syncThirdSession(param);
    }
}
