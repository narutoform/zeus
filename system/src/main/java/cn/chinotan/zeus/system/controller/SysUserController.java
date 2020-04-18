package cn.chinotan.zeus.system.controller;

import cn.chinotan.zeus.config.properties.ZeusProperties;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.chinotan.zeus.framework.core.pagination.Paging;
import cn.chinotan.zeus.framework.log.annotation.Module;
import cn.chinotan.zeus.framework.log.annotation.OperationLog;
import cn.chinotan.zeus.framework.log.enums.OperationLogType;
import cn.chinotan.zeus.system.entity.SysUser;
import cn.chinotan.zeus.system.param.sysuser.ResetPasswordParam;
import cn.chinotan.zeus.system.param.sysuser.SysUserPageParam;
import cn.chinotan.zeus.system.param.sysuser.UpdatePasswordParam;
import cn.chinotan.zeus.system.param.sysuser.UploadHeadParam;
import cn.chinotan.zeus.system.service.SysUserService;
import cn.chinotan.zeus.system.vo.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 * 系统用户 前端控制器
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
@Slf4j
@RestController
@RequestMapping("/sysUser")
@Module("system")
@Api(value = "系统用户API", tags = {"系统用户"})
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ZeusProperties zeusProperties;

    /**
     * 添加系统用户
     */
    @PostMapping("/add")
    @RequiresPermissions("sys:user:add")
    @OperationLog(name = "添加系统用户", type = OperationLogType.ADD)
    @ApiOperation(value = "添加系统用户", response = ApiResult.class)
    public ApiResult<Boolean> addSysUser(@Validated @RequestBody SysUser sysUser) throws Exception {
        boolean flag = sysUserService.saveSysUser(sysUser);
        return ApiResult.result(flag);
    }

    /**
     * 修改系统用户
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    @OperationLog(name = "修改系统用户", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改系统用户", response = ApiResult.class)
    public ApiResult<Boolean> updateSysUser(@Validated @RequestBody SysUser sysUser) throws Exception {
        boolean flag = sysUserService.updateSysUser(sysUser);
        return ApiResult.result(flag);
    }

    /**
     * 删除系统用户
     */
    @PostMapping("/delete/{id}")
    @RequiresPermissions("sys:user:delete")
    @OperationLog(name = "删除系统用户", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除系统用户", response = ApiResult.class)
    public ApiResult<Boolean> deleteSysUser(@PathVariable("id") Long id) throws Exception {
        boolean flag = sysUserService.deleteSysUser(id);
        return ApiResult.result(flag);
    }


    /**
     * 根据id获取系统用户
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:user:info:id")
    @OperationLog(name = "系统用户详情", type = OperationLogType.INFO)
    @ApiOperation(value = "系统用户详情", notes = "", response = SysUserQueryVo.class)
    public ApiResult<SysUserQueryVo> getSysUser(@PathVariable("id") Long id) throws Exception {
        SysUserQueryVo sysUserQueryVo = sysUserService.getSysUserById(id);
        return ApiResult.ok(sysUserQueryVo);
    }

    /**
     * 系统用户分页列表
     */
    @PostMapping("/getPageList")
    @RequiresPermissions("sys:user:page")
    @OperationLog(name = "系统用户分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "系统用户分页列表", response = SysUserQueryVo.class)
    public ApiResult<Paging<SysUserQueryVo>> getSysUserPageList(@Validated @RequestBody SysUserPageParam sysUserPageParam) throws Exception {
        Paging<SysUserQueryVo> paging = sysUserService.getSysUserPageList(sysUserPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    @RequiresPermissions("sys:user:update:password")
    @OperationLog(name = "修改密码", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改密码", response = ApiResult.class)
    public ApiResult<Boolean> updatePassword(@Validated @RequestBody UpdatePasswordParam updatePasswordParam) throws Exception {
        boolean flag = sysUserService.updatePassword(updatePasswordParam);
        return ApiResult.result(flag);
    }

    /**
     * 管理员重置用户密码
     */
    @PostMapping("/resetPassword")
    @RequiresPermissions("sys:user:reset:password")
    @OperationLog(name = "管理员重置用户密码", type = OperationLogType.UPDATE)
    @ApiOperation(value = "管理员重置用户密码", response = ApiResult.class)
    public ApiResult<Boolean> resetPassword(@Validated @RequestBody ResetPasswordParam resetPasswordParam) throws Exception {
        boolean flag = sysUserService.resetPassword(resetPasswordParam);
        return ApiResult.result(flag);
    }

    /**
     * 修改头像
     */
    @PostMapping("/uploadHead")
    @RequiresPermissions("sys:user:update:head")
    @OperationLog(name = "修改头像", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改头像", response = ApiResult.class)
    public ApiResult<Boolean> uploadHead(@Validated @RequestBody UploadHeadParam uploadHeadParam) throws Exception {
        boolean flag = sysUserService.updateSysUserHead(uploadHeadParam.getId(), uploadHeadParam.getHead());
        return ApiResult.result(flag);
    }
}

