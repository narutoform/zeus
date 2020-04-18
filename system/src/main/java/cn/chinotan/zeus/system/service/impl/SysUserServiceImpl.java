package cn.chinotan.zeus.system.service.impl;

import cn.chinotan.zeus.system.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.chinotan.zeus.config.properties.ZeusProperties;
import cn.chinotan.zeus.framework.common.exception.BusinessException;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.core.pagination.PageInfo;
import cn.chinotan.zeus.framework.core.pagination.Paging;
import cn.chinotan.zeus.framework.shiro.util.SaltUtil;
import cn.chinotan.zeus.framework.util.PasswordUtil;
import cn.chinotan.zeus.framework.util.PhoneUtil;
import cn.chinotan.zeus.system.entity.SysUser;
import cn.chinotan.zeus.system.enums.StateEnum;
import cn.chinotan.zeus.system.param.sysuser.ResetPasswordParam;
import cn.chinotan.zeus.system.param.sysuser.SysUserPageParam;
import cn.chinotan.zeus.system.param.sysuser.UpdatePasswordParam;
import cn.chinotan.zeus.system.service.SysDepartmentService;
import cn.chinotan.zeus.system.service.SysRoleService;
import cn.chinotan.zeus.system.service.SysUserService;
import cn.chinotan.zeus.system.vo.SysUserQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;


/**
 * <pre>
 * 系统用户 服务实现类
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
@Slf4j
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Lazy
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private ZeusProperties zeusProperties;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSysUser(SysUser sysUser) throws Exception {
        // 校验用户名是否存在
        boolean isExists = isExistsByUsername(sysUser.getUsername());
        if (isExists) {
            throw new BusinessException("用户名已存在");
        }
        // 校验部门和角色
        checkDepartmentAndRole(sysUser.getDepartmentId(), sysUser.getRoleId());
        sysUser.setId(null);

        // 生成盐值
        String salt = null;
        String password = sysUser.getPassword();
        String encryptPassword = null;
        // 如果密码为空，则设置默认密码
        if (StringUtils.isBlank(password)) {
            salt = zeusProperties.getLoginInitSalt();
            encryptPassword = zeusProperties.getLoginInitPassword();
        } else {
            salt = SaltUtil.generateSalt();
        }
        // 密码加密
        encryptPassword = PasswordUtil.encrypt(password, salt);
        sysUser.setSalt(salt);
        sysUser.setPassword(encryptPassword);

        // 如果头像为空，则设置默认头像
        if (StringUtils.isBlank(sysUser.getHead())) {
            sysUser.setHead(zeusProperties.getLoginInitHead());
        }

        // 保存系统用户
        return super.save(sysUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysUser(SysUser sysUser) throws Exception {
        // 校验部门和角色
        checkDepartmentAndRole(sysUser.getDepartmentId(), sysUser.getRoleId());
        // 获取系统用户
        SysUser updateSysUser = getById(sysUser.getId());
        if (updateSysUser == null) {
            throw new BusinessException("修改的用户不存在");
        }

        // 修改系统用户
        updateSysUser.setNickname(sysUser.getNickname())
                .setPhone(sysUser.getPhone())
                .setGender(sysUser.getGender())
                .setRemark(sysUser.getRemark())
                .setState(sysUser.getState())
                .setDepartmentId(sysUser.getDepartmentId())
                .setRoleId(sysUser.getRoleId())
                .setUpdateTime(new Date());
        return super.updateById(updateSysUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSysUser(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public SysUserQueryVo getSysUserById(Serializable id) throws Exception {
        return sysUserMapper.getSysUserById(id);
    }

    @Override
    public Paging<SysUserQueryVo> getSysUserPageList(SysUserPageParam sysUserPageParam) throws Exception {
        Page<SysUserQueryVo> page = new PageInfo<>(sysUserPageParam, OrderItem.desc(getLambdaColumn(SysUser::getCreateTime)));
        IPage<SysUserQueryVo> iPage = sysUserMapper.getSysUserPageList(page, sysUserPageParam);

        // 手机号码脱敏处理
        if (iPage != null && CollectionUtils.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(vo -> {
                vo.setPhone(PhoneUtil.desensitize(vo.getPhone()));
            });
        }

        return new Paging(iPage);
    }

    @Override
    public boolean isExistsByUsername(String username) throws Exception {
        SysUser selectSysUser = new SysUser().setUsername(username);
        return sysUserMapper.selectCount(new QueryWrapper<>(selectSysUser)) > 0;
    }

    @Override
    public void checkDepartmentAndRole(Long departmentId, Long roleId) throws Exception {
        // 校验部门是否存在并且可用
        boolean isEnableDepartment = sysDepartmentService.isEnableSysDepartment(departmentId);
        if (!isEnableDepartment) {
            throw new BusinessException("该部门不存在或已禁用");
        }
        // 校验角色是否存在并且可用
        boolean isEnableRole = sysRoleService.isEnableSysRole(roleId);
        if (!isEnableRole) {
            throw new BusinessException("该角色不存在或已禁用");
        }
    }

    @Override
    public boolean isExistsSysUserByRoleId(Long roleId) throws Exception {
        SysUser sysUser = new SysUser()
                .setState(StateEnum.ENABLE.getCode())
                .setRoleId(roleId);
        return sysUserMapper.selectCount(new QueryWrapper(sysUser)) > 0;
    }

    @Override
    public boolean updatePassword(UpdatePasswordParam updatePasswordParam) throws Exception {
        String oldPassword = updatePasswordParam.getOldPassword();
        String newPassword = updatePasswordParam.getNewPassword();
        String confirmPassword = updatePasswordParam.getConfirmPassword();
        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException("两次输入的密码不一致");
        }
        if (newPassword.equals(oldPassword)) {
            throw new BusinessException("新密码和旧密码不能一致");
        }

        // 判断原密码是否正确
        SysUser sysUser = getById(updatePasswordParam.getUserId());
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }
        if (StateEnum.DISABLE.getCode().equals(sysUser.getState())) {
            throw new BusinessException("用户已禁用");
        }
        // 密码加密处理
        String salt = sysUser.getSalt();
        String encryptOldPassword = PasswordUtil.encrypt(oldPassword, salt);
        if (!sysUser.getPassword().equals(encryptOldPassword)) {
            throw new BusinessException("原密码错误");
        }
        // 新密码加密
        String encryptNewPassword = PasswordUtil.encrypt(newPassword, salt);

        // 修改密码
        SysUser updateSysUser = new SysUser()
                .setId(sysUser.getId())
                .setPassword(encryptNewPassword)
                .setUpdateTime(new Date());
        return updateById(updateSysUser);
    }

    @Override
    public boolean updateSysUserHead(Long id, String headPath) throws Exception {
        SysUser sysUser = new SysUser()
                .setId(id)
                .setHead(headPath);
        return updateById(sysUser);
    }

    @Override
    public boolean resetPassword(ResetPasswordParam resetPasswordParam) throws Exception {
        String newPassword = resetPasswordParam.getNewPassword();
        String confirmPassword = resetPasswordParam.getConfirmPassword();
        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException("两次输入的密码不一致");
        }
        // 判断用户是否可修改
        SysUser sysUser = getById(resetPasswordParam.getUserId());
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }
        if (StateEnum.DISABLE.getCode().equals(sysUser.getState())) {
            throw new BusinessException("用户已禁用");
        }
        // 密码加密处理
        String salt = sysUser.getSalt();
        // 新密码加密
        String encryptNewPassword = PasswordUtil.encrypt(newPassword, salt);

        // 修改密码
        SysUser updateSysUser = new SysUser()
                .setId(sysUser.getId())
                .setPassword(encryptNewPassword)
                .setUpdateTime(new Date());
        return updateById(updateSysUser);
    }
}
