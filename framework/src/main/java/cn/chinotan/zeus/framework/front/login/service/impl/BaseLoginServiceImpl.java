package cn.chinotan.zeus.framework.front.login.service.impl;

import cn.chinotan.zeus.config.constant.CommonRedisKey;
import cn.chinotan.zeus.framework.common.bean.ClientInfo;
import cn.chinotan.zeus.framework.common.vo.LoginUserRedisVo;
import cn.chinotan.zeus.framework.front.login.service.BaseLoginService;
import cn.chinotan.zeus.framework.shiro.util.JwtUtil;
import cn.chinotan.zeus.framework.util.ClientInfoUtil;
import cn.chinotan.zeus.framework.util.HttpServletRequestUtil;
import cn.chinotan.zeus.framework.util.UUIDUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 登录服务实现类
 * </p>
 *
 * @author xingcheng
 * @date 2019-08-17
 **/
@Api
@Slf4j
@Service
public class BaseLoginServiceImpl implements BaseLoginService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${zeus.login.front.token-valid-time}")
    private Integer tokenValidTime;

    @Override
    public String login(Long userId) {
        Assert.notNull(userId, "userId");
        log.debug("BaseLoginServiceImpl login userId = {}", userId);

        // 创建token
        String uuid = UUIDUtil.getUuid();
        Date expiration = DateUtils.addMinutes(DateUtil.date(), tokenValidTime);
        String token = JwtUtil.create(userId, uuid, expiration);
        // login redis处理
        loginRedis(userId, token, uuid);

        return token;
    }

    /**
     * jwt生成的token携带用户id，uuid唯一标识，返回token
     *
     * @param userId
     * @param token
     * @param uuid
     */
    private void loginRedis(Long userId, String token, String uuid) {
        // 获取用户客户端信息
        HttpServletRequest request = HttpServletRequestUtil.getRequest();
        String userAgent = request.getHeader("User-Agent");
        ClientInfo clientInfo = ClientInfoUtil.get(userAgent);
        log.debug("clientInfo = " + JSON.toJSONString(clientInfo, true));

        // loginUser redis缓存信息
        LoginUserRedisVo loginUserRedisVo = new LoginUserRedisVo();
        // 设置用户客户端信息
        loginUserRedisVo.setClientInfo(clientInfo);
        // 设置系统登录用户对象
        loginUserRedisVo.setUserId(userId);
        // 设置系统登录用户token对象
        loginUserRedisVo.setToken(token);
        loginUserRedisVo.setCreateDate(DateUtil.date());
        loginUserRedisVo.setUuid(uuid);

        // user hash cache
        String loginUserRedisKey = String.format(CommonRedisKey.LOGIN_USER, userId);
        // 登录缓存
        redisTemplate.opsForValue().set(loginUserRedisKey, loginUserRedisVo, tokenValidTime, TimeUnit.MINUTES);
    }

}
