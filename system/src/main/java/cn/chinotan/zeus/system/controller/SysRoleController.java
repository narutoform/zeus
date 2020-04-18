package cn.chinotan.zeus.system.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.chinotan.zeus.framework.core.pagination.Paging;
import cn.chinotan.zeus.framework.core.validator.groups.Add;
import cn.chinotan.zeus.framework.core.validator.groups.Update;
import cn.chinotan.zeus.framework.log.annotation.Module;
import cn.chinotan.zeus.framework.log.annotation.OperationLog;
import cn.chinotan.zeus.framework.log.enums.OperationLogType;
import cn.chinotan.zeus.system.entity.SysRole;
import cn.chinotan.zeus.system.param.sysrole.SysRolePageParam;
import cn.chinotan.zeus.system.param.sysrole.UpdateSysRolePermissionParam;
import cn.chinotan.zeus.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>
 * 系统角色 前端控制器
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
@Slf4j
@RestController
@RequestMapping("/sysRole")
@Module("system")
@Api(value = "系统角色API", tags = {"系统角色"})
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 添加系统角色
     */
    @PostMapping("/add")
    @RequiresPermissions("sys:role:add")
    @OperationLog(name = "添加系统角色", type = OperationLogType.ADD)
    @ApiOperation(value = "添加系统角色", response = ApiResult.class)
    public ApiResult<Boolean> addSysRole(@Validated(Add.class) @RequestBody SysRole sysRole) throws Exception {
        boolean flag = sysRoleService.saveSysRole(sysRole);
        return ApiResult.result(flag);
    }

    /**
     * 修改系统角色
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    @OperationLog(name = "修改系统角色", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改系统角色", response = ApiResult.class)
    public ApiResult<Boolean> updateSysRole(@Validated(Update.class) @RequestBody SysRole sysRole) throws Exception {
        boolean flag = sysRoleService.updateSysRole(sysRole);
        return ApiResult.result(flag);
    }

    /**
     * 删除系统角色
     */
    @PostMapping("/delete/{id}")
    @RequiresPermissions("sys:role:delete")
    @OperationLog(name = "删除系统角色", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除系统角色", response = ApiResult.class)
    public ApiResult<Boolean> deleteSysRole(@PathVariable("id") Long id) throws Exception {
        boolean flag = sysRoleService.deleteSysRole(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取系统角色
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:role:info")
    @OperationLog(name = "系统角色详情", type = OperationLogType.INFO)
    @ApiOperation(value = "系统角色详情", response = SysRole.class)
    public ApiResult<SysRole> getSysRole(@PathVariable("id") Long id) throws Exception {
        SysRole sysRole = sysRoleService.getById(id);
        return ApiResult.ok(sysRole);
    }

    /**
     * 系统角色分页列表
     */
    @PostMapping("/getPageList")
    @RequiresPermissions("sys:role:page")
    @OperationLog(name = "系统角色分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "系统角色分页列表", response = SysRole.class)
    public ApiResult<Paging<SysRole>> getSysRolePageList(@Validated @RequestBody SysRolePageParam sysRolePageParam) throws Exception {
        Paging<SysRole> paging = sysRoleService.getSysRolePageList(sysRolePageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 获取系统角色列表
     *
     * @return
     */
    @PostMapping("/getList")
    @RequiresPermissions("sys:role:list")
    @OperationLog(name = "系统角色列表", type = OperationLogType.LIST)
    @ApiOperation(value = "系统角色列表", response = SysRole.class)
    public ApiResult<List<SysRole>> getRoleList() {
        return ApiResult.ok(sysRoleService.list());
    }

    /**
     * 修改系统角色权限
     */
    @PostMapping("/updateSysRolePermission")
    @RequiresPermissions("sys:role-permission:update")
    @OperationLog(name = "修改系统角色权限", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改系统角色权限", response = ApiResult.class)
    public ApiResult<Boolean> updateSysRolePermission(@Validated @RequestBody UpdateSysRolePermissionParam param) throws Exception {
        boolean flag = sysRoleService.updateSysRolePermission(param);
        return ApiResult.result(flag);
    }

}

