package cn.chinotan.zeus.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.chinotan.zeus.system.entity.SysRolePermission;
import cn.chinotan.zeus.system.param.sysrole.SysRolePermissionPageParam;
import cn.chinotan.zeus.system.vo.SysRolePermissionQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 * 角色权限关系 Mapper 接口
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
@Repository
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    SysRolePermissionQueryVo getSysRolePermissionById(Serializable id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param sysRolePermissionPageParam
     * @return
     */
    IPage<SysRolePermissionQueryVo> getSysRolePermissionPageList(@Param("page") Page page, @Param("param") SysRolePermissionPageParam sysRolePermissionPageParam);

    /**
     * 根据角色id获取可用的权限编码
     *
     * @param roleId
     * @return
     */
    Set<String> getPermissionCodesByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色id获取该对应的所有三级权限ID
     * @param roleId
     * @return
     */
    List<Long> getThreeLevelPermissionIdsByRoleId(@Param("roleId") Long roleId);
}
