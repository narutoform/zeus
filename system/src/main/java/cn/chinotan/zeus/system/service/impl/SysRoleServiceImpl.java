package cn.chinotan.zeus.system.service.impl;

import cn.chinotan.zeus.system.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.chinotan.zeus.framework.common.exception.BusinessException;
import cn.chinotan.zeus.framework.common.exception.DaoException;
import cn.chinotan.zeus.framework.common.exception.ZeusException;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.core.pagination.PageInfo;
import cn.chinotan.zeus.framework.core.pagination.Paging;
import cn.chinotan.zeus.system.entity.SysRole;
import cn.chinotan.zeus.system.entity.SysRolePermission;
import cn.chinotan.zeus.system.enums.StateEnum;
import cn.chinotan.zeus.system.param.sysrole.SysRolePageParam;
import cn.chinotan.zeus.system.param.sysrole.UpdateSysRolePermissionParam;
import cn.chinotan.zeus.system.service.SysPermissionService;
import cn.chinotan.zeus.system.service.SysRolePermissionService;
import cn.chinotan.zeus.system.service.SysRoleService;
import cn.chinotan.zeus.system.service.SysUserService;
import cn.chinotan.zeus.system.vo.SysRoleQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.SetUtils.SetView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * <pre>
 * 系统角色 服务实现类
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    @Autowired
    private SysUserService sysUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSysRole(SysRole sysRole) throws Exception {
        String code = sysRole.getCode();
        // 校验角色标识code唯一性
        if (isExistsByCode(code)) {
            throw new BusinessException("角色编码已存在");
        }
        // 保存角色
        boolean saveRoleResult = super.save(sysRole);
        if (!saveRoleResult) {
            throw new DaoException("保存角色失败");
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysRole(SysRole sysRole) throws Exception {
        Long roleId = sysRole.getId();
        // 校验角色是否存在
        if (getById(roleId) == null) {
            throw new BusinessException("该角色不存在");
        }
        // 修改角色
        sysRole.setUpdateTime(new Date());
        boolean updateResult = updateById(sysRole);
        if (!updateResult) {
            throw new DaoException("修改系统角色失败");
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSysRole(Long id) throws Exception {
        // 判断该角色下是否有可用用户，如果有，则不能删除
        boolean isExistsUser = sysUserService.isExistsSysUserByRoleId(id);
        if (isExistsUser) {
            throw new DaoException("该角色下还存在可用用户，不能删除");
        }
        // 角色真实删除
        boolean deleteRoleResult = removeById(id);
        if (!deleteRoleResult) {
            throw new DaoException("删除角色失败");
        }

        // 判断角色是否有权限，如果有，则删除
        boolean hasPermission = sysRolePermissionService.hasPermission(id);
        if (hasPermission) {
            // 角色权限关系真实删除
            boolean deletePermissionResult = sysRolePermissionService.deleteSysRolePermissionByRoleId(id);
            if (!deletePermissionResult) {
                throw new DaoException("删除角色权限关系失败");
            }
        }
        return true;
    }

    @Override
    public SysRoleQueryVo getSysRoleById(Serializable id) throws Exception {
        SysRoleQueryVo sysRoleQueryVo = sysRoleMapper.getSysRoleById(id);
        if (sysRoleQueryVo == null) {
            throw new ZeusException("角色不存在");
        }
        List<Long> permissionIds = sysRolePermissionService.getPermissionIdsByRoleId((Long) id);
        sysRoleQueryVo.setPermissions(new HashSet<>(permissionIds));
        return sysRoleQueryVo;
    }

    @Override
    public Paging<SysRole> getSysRolePageList(SysRolePageParam sysRolePageParam) throws Exception {
        Page<SysRole> page = new PageInfo<>(sysRolePageParam, OrderItem.desc(getLambdaColumn(SysRole::getCreateTime)));
        // 此处演示单表，使用mybatisplus自带方法进行分页
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String keyword = sysRolePageParam.getKeyword();
        String name = sysRolePageParam.getName();
        String code = sysRolePageParam.getCode();
        Integer state = sysRolePageParam.getState();
        if (StringUtils.isNotBlank(keyword)) {
            lambdaQueryWrapper
                    .like(SysRole::getName, keyword)
                    .or()
                    .like(SysRole::getCode, keyword);
        }
        if (StringUtils.isNotBlank(name)) {
            lambdaQueryWrapper.like(SysRole::getName, name);
        }
        if (StringUtils.isNotBlank(code)) {
            lambdaQueryWrapper.like(SysRole::getCode, code);
        }
        if (state != null) {
            lambdaQueryWrapper.eq(SysRole::getState, state);
        }
        IPage<SysRole> iPage = sysRoleMapper.selectPage(page, lambdaQueryWrapper);
        return new Paging<SysRole>(iPage);
    }

    @Override
    public boolean isEnableSysRole(Long id) throws Exception {
        SysRole sysRole = new SysRole()
                .setId(id)
                .setState(StateEnum.ENABLE.getCode());
        int count = sysRoleMapper.selectCount(new QueryWrapper<>(sysRole));
        return count > 0;
    }

    @Override
    public boolean isExistsByCode(String code) throws Exception {
        SysRole sysRole = new SysRole().setCode(code);
        return sysRoleMapper.selectCount(new QueryWrapper<>(sysRole)) > 0;
    }

    @Override
    public boolean updateSysRolePermission(UpdateSysRolePermissionParam param) throws Exception {
        Long roleId = param.getRoleId();
        List<Long> permissionIds = param.getPermissionIds();
        // 校验角色是否存在
        SysRole sysRole = getById(roleId);
        if (sysRole == null) {
            throw new BusinessException("该角色不存在");
        }
        if (CollectionUtils.isNotEmpty(permissionIds)) {
            // 校验权限列表是否存在
            if (!sysPermissionService.isExistsByPermissionIds(permissionIds)) {
                throw new BusinessException("权限列表id匹配失败");
            }
        }
        // 获取之前的权限id集合
        List<Long> beforeList = sysRolePermissionService.getPermissionIdsByRoleId(roleId);
        // 差集计算
        // before：1,2,3,4,5,6
        // after： 1,2,3,4,7,8
        // 删除5,6 新增7,8
        // 此处真实删除，去掉deleted字段的@TableLogic注解
        Set<Long> beforeSet = new HashSet<>(beforeList);
        Set<Long> afterSet = new HashSet<>(permissionIds);
        SetView<Long> deleteSet = SetUtils.difference(beforeSet, afterSet);
        SetView<Long> addSet = SetUtils.difference(afterSet, beforeSet);
        log.debug("deleteSet = " + deleteSet);
        log.debug("addSet = " + addSet);

        if (CollectionUtils.isNotEmpty(deleteSet)) {
            // 删除权限关联
            LambdaUpdateWrapper<SysRolePermission> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SysRolePermission::getRoleId, roleId);
            updateWrapper.in(SysRolePermission::getPermissionId, deleteSet);
            boolean deleteResult = sysRolePermissionService.remove(updateWrapper);
            if (!deleteResult) {
                throw new DaoException("删除角色权限关系失败");
            }
        }

        if (CollectionUtils.isNotEmpty(addSet)) {
            // 新增权限关联
            boolean addResult = sysRolePermissionService.saveSysRolePermissionBatch(roleId, addSet);
            if (!addResult) {
                throw new DaoException("新增角色权限关系失败");
            }
        }

        return true;
    }

}
