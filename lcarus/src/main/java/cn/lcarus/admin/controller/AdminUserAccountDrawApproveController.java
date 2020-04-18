package cn.lcarus.admin.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.admin.param.UserAccountDrawApproveQueryParam;
import cn.lcarus.admin.param.UserAccountDrawApproveSubmitParam;
import cn.lcarus.admin.res.UseAccountDrawApproveVo;
import cn.lcarus.common.service.BaseUserAccountDrawApproveService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xincheng
 * @since 2019-10-24
 */
@Slf4j
@RestController
@RequestMapping("/userAccountDrawApprove")
@Api("用户账户提现审核相关接口 API")
public class AdminUserAccountDrawApproveController extends BaseController {

    @Autowired
    private BaseUserAccountDrawApproveService baseUserAccountDrawApproveService;

    @GetMapping("/v1/list")
    @ApiOperation(value = "列表展示", notes = "接口")
    public ApiResult<Page<UseAccountDrawApproveVo>> showList(@Valid UserAccountDrawApproveQueryParam param) {
        return baseUserAccountDrawApproveService.showList(param);
    }

    @PostMapping("/v1/approve")
    @ApiOperation(value = "审批接口", notes = "接口")
    public ApiResult<?> approve(@Valid @RequestBody UserAccountDrawApproveSubmitParam param) {
        return baseUserAccountDrawApproveService.approve(param);
    }
}

