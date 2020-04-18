package cn.lcarus.guess.web.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.common.entity.UserAccountRecord;
import cn.lcarus.common.service.BaseUserAccountService;
import cn.lcarus.guess.web.param.LoginUserPageVo;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserWithDrewParam;
import cn.lcarus.guess.web.res.UserAccountInfoRes;
import cn.lcarus.guess.web.res.UserAccountRecordInfoRes;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/api/user/account")
@Api(value = "用户账户接口 API", description = "05.用户账户接口")
public class UserAccountController extends BaseController {

    @Autowired
    private BaseUserAccountService baseUserAccountService;

    @PostMapping("/v1/info")
    @ApiOperation(value = "用户账户信息展示接口")
    public ApiResult<UserAccountInfoRes> showInfo(@Valid @RequestBody LoginUserVo param) {
        return baseUserAccountService.showInfo(param);
    }

    @PostMapping("/v1/cashWithdrawal")
    @ApiOperation(value = "用户红包提现接口")
    public ApiResult<?> cashWithdrawal(@Valid @RequestBody UserWithDrewParam param) {
        return baseUserAccountService.cashWithdrawal(param);
    }

    @PostMapping("/v1/record")
    @ApiOperation(value = "用户账户收支明细展示接口")
    public ApiResult<Page<UserAccountRecordInfoRes>> showRecord(@Valid @RequestBody LoginUserPageVo<UserAccountRecord> param) {
        return baseUserAccountService.showRecord(param);
    }
}
