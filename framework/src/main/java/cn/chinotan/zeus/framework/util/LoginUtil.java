package cn.chinotan.zeus.framework.util;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.config.constant.CommonRedisKey;
import cn.chinotan.zeus.framework.shiro.util.JwtTokenUtil;
import cn.chinotan.zeus.framework.shiro.util.JwtUtil;
import cn.chinotan.zeus.framework.shiro.vo.LoginSysUserRedisVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


/**
 * 获取登录信息工具类
 *
 * @author xingcheng
 * @date 2018-11-08
 */
@Slf4j
@Component
public class LoginUtil {

    private static RedisTemplate redisTemplate;

    public LoginUtil(RedisTemplate redisTemplate) {
        LoginUtil.redisTemplate = redisTemplate;
    }

    /**
     * 获取当前登录用户对象
     *
     * @return
     */
    public static LoginSysUserRedisVo getLoginSysUserRedisVo() {
        // 获取当前登录用户
        String token = JwtTokenUtil.getToken();
        String username = JwtUtil.getUsername(token);
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return (LoginSysUserRedisVo) redisTemplate.opsForValue().get(String.format(CommonRedisKey.LOGIN_USER, username));
    }

    /**
     * 获取当前登录用户的ID
     *
     * @return
     */
    public static Long getUserId() {
        LoginSysUserRedisVo loginSysUserRedisVo = getLoginSysUserRedisVo();
        if (loginSysUserRedisVo == null) {
            return null;
        }
        return loginSysUserRedisVo.getId();
    }

    /**
     * 获取当前登录用户的账号
     *
     * @return
     */
    public static String getUsername() {
        LoginSysUserRedisVo loginSysUserRedisVo = getLoginSysUserRedisVo();
        if (loginSysUserRedisVo == null) {
            return null;
        }
        return loginSysUserRedisVo.getUsername();
    }

    /**
     * 从请求头或者请求参数中
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader(CommonConstant.TOKEN);
        if (StringUtils.isBlank(token)) {
            // 从请求参数中获取token
            token = request.getParameter(CommonConstant.TOKEN);
        }
        return token;
    }
}
