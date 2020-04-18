package cn.lcarus.guess.web.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.common.service.BaseUserThirdNoticeService;
import cn.lcarus.guess.web.param.UserThirdSubscribeParam;
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
 * @description: 用户三方订阅消息相关接口
 * @author: xingcheng
 * @create: 2020-03-02 21:12
 **/
@Slf4j
@RestController
@RequestMapping("/api/userThirdNotice")
@Api(value = "用户三方订阅消息相关接口 API", description = "08.用户三方订阅消息关接口")
public class UserThirdNoticeController extends BaseController {

    @Autowired
    private BaseUserThirdNoticeService baseUserThirdNoticeService;

    @PostMapping("/v1/subscribeResult")
    @ApiOperation(value = "用户订阅结果请求接口")
    public ApiResult<?> subscribeResult(@Valid @RequestBody UserThirdSubscribeParam param) {
        return baseUserThirdNoticeService.subscribeResult(param);
    }
}
