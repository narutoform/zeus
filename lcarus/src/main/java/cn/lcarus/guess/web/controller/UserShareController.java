package cn.lcarus.guess.web.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.common.service.BaseUserShareRecordService;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserShareDrawParam;
import cn.lcarus.guess.web.res.UserShareDrawRes;
import cn.lcarus.guess.web.res.UserShareResultRes;
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
 * @author: xingcheng
 * @create: 2020-03-02 21:12
 **/
@Slf4j
@RestController
@RequestMapping("/api/user/share")
@Api(value = "用户分享接口 API", description = "04.用户分享接口")
public class UserShareController extends BaseController {

    @Autowired
    private BaseUserShareRecordService baseUserShareRecordService;

    @PostMapping("/v1/result")
    @ApiOperation(value = "用户分享结果回调接口 已弃用")
    @Deprecated
    public ApiResult<UserShareResultRes> shareResult(@Valid @RequestBody LoginUserVo param) {
        return baseUserShareRecordService.shareResult(param);
    }

    @PostMapping("/v1/draw")
    @ApiOperation(value = "用户分享结果回调后领取红包接口  已弃用")
    @Deprecated
    public ApiResult<UserShareDrawRes> result(@Valid @RequestBody UserShareDrawParam param) {
        return baseUserShareRecordService.draw(param);
    }

    @PostMapping("/v2/result")
    @ApiOperation(value = "用户分享结果回调接口 v2")
    public ApiResult<UserShareResultRes> shareResultCallback(@Valid @RequestBody LoginUserVo param) {
        return baseUserShareRecordService.shareResultCallback(param);
    }

    @PostMapping("/v2/draw")
    @ApiOperation(value = "用户分享结果回调后领取红包接口  v2")
    public ApiResult<UserShareDrawRes> drawCall(@Valid @RequestBody LoginUserVo param) {
        return baseUserShareRecordService.drawCall(param);
    }
}
