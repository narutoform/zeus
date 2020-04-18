package cn.chinotan.zeus.framework.front.intercepter;

import cn.chinotan.zeus.config.constant.CommonRedisKey;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.vo.LoginUserRedisVo;
import cn.chinotan.zeus.framework.shiro.util.JwtUtil;
import cn.chinotan.zeus.framework.util.HttpServletResponseUtil;
import cn.chinotan.zeus.framework.util.LoginUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * <p>
 * 判断jwt token是否有效
 * </p>
 *
 * @author xingcheng
 * @date 2019-08-17
 **/
@Slf4j
public class JwtInterceptor extends HandlerInterceptorAdapter {

    public final static String USER_ID = "userId";

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${guess.user.login.enable-redis-check}")
    private boolean redisCheck;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果访问的不是控制器,则跳出,继续执行下一个拦截器
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 获取token
        String token = LoginUtil.getToken(request);

        // 如果token为空，则s输出提示并返回
        if (StringUtils.isBlank(token)) {
            ApiResult apiResult = ApiResult.result(ApiCode.UNAUTHORIZED);
            log.error("token is empty");
            HttpServletResponseUtil.printJson(response, apiResult);
            return false;
        }

        log.debug("token:{}", token);

        // 验证token是否有效
        Jws<Claims> jws = JwtUtil.verify(token, true);
        log.debug("token verify:{}", jws);
        if (jws == null || jws.getBody() == null) {
            ApiResult apiResult = ApiResult.result(ApiCode.UNAUTHORIZED);
            log.error("token verify failed");
            HttpServletResponseUtil.printJson(response, apiResult);
            return false;
        }
        String userId = jws.getBody().getAudience();
        String id = jws.getBody().getId();
        if (StringUtils.isBlank(userId)) {
            ApiResult apiResult = ApiResult.result(ApiCode.UNAUTHORIZED);
            log.error("token verify userId is blank");
            HttpServletResponseUtil.printJson(response, apiResult);
            return false;
        }

        // 验证缓存
        if (redisCheck) {
            String loginUserRedisKey = String.format(CommonRedisKey.LOGIN_USER, userId);
            LoginUserRedisVo userTokenCache = (LoginUserRedisVo) redisTemplate.opsForValue().get(loginUserRedisKey);
            if (userTokenCache == null) {
                ApiResult apiResult = ApiResult.result(ApiCode.UNAUTHORIZED);
                log.error("token verify userTokenCache is blank");
                HttpServletResponseUtil.printJson(response, apiResult);
                return false;
            } else {
                // uuid验证
                String uuid = userTokenCache.getUuid();
                if (!Objects.equals(uuid, id)) {
                    ApiResult apiResult = ApiResult.result(ApiCode.UNAUTHORIZED);
                    log.error("token verify uuid is not equal");
                    HttpServletResponseUtil.printJson(response, apiResult);
                    return false;
                }
                // token验证
                String tokenCache = userTokenCache.getToken();
                if (!Objects.equals(tokenCache, token)) {
                    ApiResult apiResult = ApiResult.result(ApiCode.UNAUTHORIZED);
                    log.error("token verify tokenCache not equal");
                    HttpServletResponseUtil.printJson(response, apiResult);
                    return false;
                }
            }
        }

        log.debug("token verify success");
        return true;
    }

}
