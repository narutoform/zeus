package cn.chinotan.zeus.system.service;

import cn.chinotan.zeus.system.entity.SysUser;
import cn.chinotan.zeus.system.param.LoginParam;
import cn.chinotan.zeus.system.vo.LoginSysUserTokenVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 登录服务接口
 * </p>
 *
 * @author xingcheng
 * @date 2019-05-23
 **/
public interface LoginService {

    /**
     * 登录
     *
     * @param loginParam
     * @return
     * @throws Exception
     */
    LoginSysUserTokenVo login(LoginParam loginParam) throws Exception;


    /**
     * 退出
     * @param request
     * @throws Exception
     */
    void logout(HttpServletRequest request) throws Exception;

    /**
     * 根据用户名获取系统用户对象
     *
     * @param username
     * @return
     * @throws Exception
     */
    SysUser getSysUserByUsername(String username) throws Exception;

    /**
     * 检查验证码是否正确
     *
     * @param verifyToken
     * @param code
     * @throws Exception
     */
    void checkVerifyCode(String verifyToken, String code) throws Exception;

}
