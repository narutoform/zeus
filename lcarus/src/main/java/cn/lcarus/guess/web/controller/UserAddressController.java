package cn.lcarus.guess.web.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.common.service.BaseUserAddressService;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserAddressAddParam;
import cn.lcarus.guess.web.res.UserAddressListRes;
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
@RequestMapping("/api/userAddress")
@Api(value = "用户地址相关接口 API", description = "07.用户地址相关接口")
public class UserAddressController extends BaseController {

    @Autowired
    private BaseUserAddressService baseUserAddressService;

    @PostMapping("/v1/list")
    @ApiOperation(value = "用户地址信息列表请求接口")
    public ApiResult<UserAddressListRes> showList(@Valid @RequestBody LoginUserVo param) {
        return baseUserAddressService.showList(param);
    }

    @PostMapping("/v1/add")
    @ApiOperation(value = "用户地址信息添加请求接口")
    public ApiResult<Long> add(@Valid @RequestBody UserAddressAddParam param) {
        return baseUserAddressService.add(param);
    }
}
