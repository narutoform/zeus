package cn.chinotan.zeus.system.service;

import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.chinotan.zeus.framework.core.pagination.Paging;
import cn.chinotan.zeus.system.entity.SysRole;
import cn.chinotan.zeus.system.vo.SysRoleQueryVo;
import cn.chinotan.zeus.system.param.sysrole.SysRolePageParam;
import cn.chinotan.zeus.system.param.sysrole.UpdateSysRolePermissionParam;

import java.io.Serializable;

/**
 * <pre>
 * 系统角色 服务类
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
public interface SysRoleService extends BaseService<SysRole> {

    /**
     * 保存
     *
     * @param addSysRoleParam
     * @return
     * @throws Exception
     */
    boolean saveSysRole(SysRole sysRole) throws Exception;

    /**
     * 修改
     *
     * @param updateSysRoleParam
     * @return
     * @throws Exception
     */
    boolean updateSysRole(SysRole sysRole) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysRole(Long id) throws Exception;

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysRoleQueryVo getSysRoleById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param sysRolePageParam
     * @return
     * @throws Exception
     */
    Paging<SysRole> getSysRolePageList(SysRolePageParam sysRolePageParam) throws Exception;

    /**
     * 根据id校验角色是否存在并且已启用
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean isEnableSysRole(Long id) throws Exception;

    /**
     * 判断角色编码是否存在
     *
     * @param code
     * @return
     * @throws Exception
     */
    boolean isExistsByCode(String code) throws Exception;

    /**
     * 修改系统角色权限配置
     *
     * @param param
     * @return
     * @throws Exception
     */
    boolean updateSysRolePermission(UpdateSysRolePermissionParam param) throws Exception;
}
