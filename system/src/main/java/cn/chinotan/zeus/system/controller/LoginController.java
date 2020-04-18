package cn.chinotan.zeus.system.controller;

import cn.chinotan.zeus.system.param.LoginParam;
import com.alibaba.fastjson.JSON;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.log.annotation.Module;
import cn.chinotan.zeus.framework.log.annotation.OperationLogIgnore;
import cn.chinotan.zeus.framework.shiro.util.JwtTokenUtil;
import cn.chinotan.zeus.system.service.LoginService;
import cn.chinotan.zeus.system.service.SysUserService;
import cn.chinotan.zeus.system.vo.LoginSysUserTokenVo;
import cn.chinotan.zeus.system.vo.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录控制器
 *
 * @author xingcheng
 * @date 2019-09-28
 * @since 1.3.0.RELEASE
 **/
@Slf4j
@RestController
@Module("system")
@Api(value = "系统登录API", tags = {"系统登录"})
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    @OperationLogIgnore
    @ApiOperation(value = "登录", notes = "系统用户登录", response = LoginSysUserTokenVo.class)
    public ApiResult<LoginSysUserTokenVo> login(@Validated @RequestBody LoginParam loginParam, HttpServletResponse response) throws Exception {
        LoginSysUserTokenVo loginSysUserTokenVo = loginService.login(loginParam);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName(), loginSysUserTokenVo.getToken());
        return ApiResult.ok(loginSysUserTokenVo, "登录成功");
    }


    /**
     * 根据token获取系统登录用户信息
     *
     * @return
     */
    @GetMapping("/getSysUserInfo")
    @ApiOperation(value = "根据token获取系统登录用户信息", response = SysUserQueryVo.class)
    public ApiResult<JSON> getSysUser() throws Exception {
//        String token =  JwtTokenUtil.getToken();
//        String tokenSha256 = DigestUtils.sha256Hex(token);
//        LoginSysUserVo loginSysUserVo = (LoginSysUserVo) redisTemplate.opsForValue().get(tokenSha256);
//        return ApiResult.ok(loginSysUserVo);

        String json = "{\n" +
                "    roles: ['admin'],\n" +
                "    introduction: 'I am a super administrator',\n" +
                "    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',\n" +
                "    name: 'Super Admin'\n" +
                "  }";
        JSON array = JSON.parseObject(json);

        return ApiResult.ok(array);
    }

    @PostMapping("/logout")
    @OperationLogIgnore
    public ApiResult<String> logout(HttpServletRequest request) throws Exception {
        loginService.logout(request);
        return ApiResult.ok("退出成功");
    }

}
