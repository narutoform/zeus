package cn.chinotan.zeus.system.service;

import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.chinotan.zeus.framework.core.pagination.Paging;
import cn.chinotan.zeus.system.entity.SysUser;
import cn.chinotan.zeus.system.param.sysuser.ResetPasswordParam;
import cn.chinotan.zeus.system.param.sysuser.SysUserPageParam;
import cn.chinotan.zeus.system.param.sysuser.UpdatePasswordParam;
import cn.chinotan.zeus.system.vo.SysUserQueryVo;

import java.io.Serializable;

/**
 * <pre>
 * 系统用户 服务类
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
public interface SysUserService extends BaseService<SysUser> {

    /**
     * 保存
     *
     * @param sysUser
     * @return
     * @throws Exception
     */
    boolean saveSysUser(SysUser sysUser) throws Exception;

    /**
     * 修改
     *
     * @param sysUser
     * @return
     * @throws Exception
     */
    boolean updateSysUser(SysUser sysUser) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysUser(Long id) throws Exception;

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysUserQueryVo getSysUserById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param sysUserPageParam
     * @return
     * @throws Exception
     */
    Paging<SysUserQueryVo> getSysUserPageList(SysUserPageParam sysUserPageParam) throws Exception;

    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     * @throws Exception
     */
    boolean isExistsByUsername(String username) throws Exception;

    /**
     * 检验部门和角色是否存在并且已启用
     *
     * @param departmentId
     * @param roleId
     * @throws Exception
     */
    void checkDepartmentAndRole(Long departmentId, Long roleId) throws Exception;

    /**
     * 通过角色id判断是否存在可用用户id
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    boolean isExistsSysUserByRoleId(Long roleId) throws Exception;

    /**
     * 修改密码
     *
     * @param updatePasswordParam
     * @return
     * @throws Exception
     */
    boolean updatePassword(UpdatePasswordParam updatePasswordParam) throws Exception;

    /**
     * 修改系统用户头像
     *
     * @param id
     * @param headPath
     * @return
     * @throws Exception
     */
    boolean updateSysUserHead(Long id, String headPath) throws Exception;

    /**
     * 管理员重置用户密码
     *
     * @param resetPasswordParam
     * @return
     * @throws Exception
     */
    boolean resetPassword(ResetPasswordParam resetPasswordParam) throws Exception;
}
