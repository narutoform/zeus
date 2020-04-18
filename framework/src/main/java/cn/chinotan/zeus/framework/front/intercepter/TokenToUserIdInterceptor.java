package cn.chinotan.zeus.framework.front.intercepter;

import cn.chinotan.zeus.framework.common.vo.CacheRequestWrapper;
import cn.chinotan.zeus.framework.shiro.util.JwtUtil;
import cn.chinotan.zeus.framework.util.LoginUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * <p>
 * token转userId拦截器
 * </p>
 *
 * @author xingcheng
 * @date 2019-08-17
 **/
@Slf4j
public class TokenToUserIdInterceptor extends HandlerInterceptorAdapter {

    public final static String USER_ID = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果访问的不是控制器,则跳出,继续执行下一个拦截器
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 获取token
        String token = LoginUtil.getToken(request);
        // 如果token为空，则不处理
        if (StringUtils.isBlank(token)) {
            return true;
        }
        // 验证token是否有效
        Jws<Claims> jws = JwtUtil.verify(token, false);
        if (Objects.isNull(jws) || Objects.isNull(jws.getBody())) {
            // token无效则不处理
            return true;
        }
        String userId = jws.getBody().getAudience();
        if (StringUtils.isBlank(userId)) {
            // userId为空则不处理
            return true;
        }
        // 获取userId并且放入body
        CacheRequestWrapper myRequestWrapper;
        if (request instanceof CacheRequestWrapper) {
            myRequestWrapper = (CacheRequestWrapper) request;
        } else {
            myRequestWrapper = new CacheRequestWrapper(request);
        }
        JSONObject oldBody = JSONObject.parseObject(myRequestWrapper.getBody());
        oldBody.put(USER_ID, userId);
        myRequestWrapper.setBody(JSON.toJSONString(oldBody));
        return true;
    }

}
