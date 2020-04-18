package cn.chinotan.zeus.framework.shiro.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.config.properties.JwtProperties;
import cn.chinotan.zeus.framework.util.UUIDUtil;
import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * https://github.com/auth0/java-jwt
 *
 * @author xingcheng
 * @date 2019-09-30
 * @since 1.3.0.RELEASE
 **/
@Slf4j
@Component
public class JwtUtil {

    private static final String UTF8 = "UTF-8";
    
    private static JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties) {
        JwtUtil.jwtProperties = jwtProperties;
        log.info(JSON.toJSONString(JwtUtil.jwtProperties));
    }

    /**
     * 生成JWT Token
     *
     * @param username       用户名
     * @param salt           盐值
     * @param expireDuration 过期时间和单位
     * @return token
     */
    public static String generateToken(String username, String salt, Duration expireDuration) {
        try {
            if (StringUtils.isBlank(username)) {
                log.error("username不能为空");
                return null;
            }
            log.debug("username:{}", username);

            // 如果盐值为空，则使用默认值：666666
            if (StringUtils.isBlank(salt)) {
                salt = jwtProperties.getSecret();
            }
            log.debug("salt:{}", salt);

            // 过期时间，单位：秒
            Long expireSecond;
            // 默认过期时间为1小时
            if (expireDuration == null) {
                expireSecond = jwtProperties.getExpireSecond();
            } else {
                expireSecond = expireDuration.getSeconds();
            }
            log.debug("expireSecond:{}", expireSecond);
            Date expireDate = DateUtils.addSeconds(new Date(), expireSecond.intValue());
            log.debug("expireDate:{}", expireDate);

            // 生成token
            Algorithm algorithm = Algorithm.HMAC256(salt);
            String token = JWT.create()
                    .withClaim(CommonConstant.JWT_USERNAME, username)
                    // jwt唯一id
                    .withJWTId(UUIDUtil.getUuid())
                    // 签发人
                    .withIssuer(jwtProperties.getIssuer())
                    // 主题
                    .withSubject(jwtProperties.getSubject())
                    // 签发的目标
                    .withAudience(jwtProperties.getAudience())
                    // 签名时间
                    .withIssuedAt(new Date())
                    // token过期时间
                    .withExpiresAt(expireDate)
                    // 签名
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            log.error("generateToken exception", e);
        }
        return null;
    }

    public static boolean verifyToken(String token, String salt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    // 签发人
                    .withIssuer(jwtProperties.getIssuer())
                    // 主题
                    .withSubject(jwtProperties.getSubject())
                    // 签发的目标
                    .withAudience(jwtProperties.getAudience())
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt != null) {
                return true;
            }
        } catch (Exception e) {
            log.error("Verify Token Exception", e);
        }
        return false;
    }

    /**
     * 解析token，获取token数据
     *
     * @param token
     * @return
     */
    public static DecodedJWT getJwtInfo(String token) {
        return JWT.decode(token);
    }

    /**
     * 获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        String username = decodedJwt.getClaim(CommonConstant.JWT_USERNAME).asString();
        return username;
    }

    /**
     * 获取创建时间
     *
     * @param token
     * @return
     */
    public static Date getIssuedAt(String token) {
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        return decodedJwt.getIssuedAt();
    }

    /**
     * 获取过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpireDate(String token) {
        DecodedJWT decodedJwt = getJwtInfo(token);
        if (decodedJwt == null) {
            return null;
        }
        return decodedJwt.getExpiresAt();
    }

    /**
     * 判断token是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpired(String token) {
        Date expireDate = getExpireDate(token);
        if (expireDate == null) {
            return true;
        }
        return expireDate.before(new Date());
    }

    /**
     * 生成jwt front token
     *
     * @return jwt token
     */
    public static String create(Long userId, String uuid, Date expiration) {
        try {
            Map<String, Object> headerClaims = Maps.newHashMap();
            Header header = new DefaultJwsHeader();
            header.setContentType(Header.CONTENT_TYPE);
            header.setType(Header.JWT_TYPE);
            headerClaims.putAll(header);

            // 构建token
            String token = Jwts.builder()
                    .setHeader(headerClaims)
                    // jwt唯一id
                    .setId(uuid)
                    // 签发人
                    .setIssuer(jwtProperties.getIssuer())
                    // 主题
                    .setSubject(jwtProperties.getSubject())
                    // 签发的目标
                    .setAudience(String.valueOf(userId))
                    // 签名时间
                    .setIssuedAt(new Date())
                    // 设置有效期
                    .setExpiration(expiration)
                    // 签名
                    .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes(UTF8))
                    .compact();
            return token;
        } catch (Exception e) {
            log.error("create token exception", e);
        }
        return null;
    }


    /**
     * 验证jwt front token
     *
     * @param token jwt token
     * @param isPrintError true
     * @return Jws对象
     */
    public static Jws<Claims> verify(String token, boolean isPrintError) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret().getBytes(UTF8))
                    .parseClaimsJws(token);
            return jws;
        } catch (Exception e) {
            if (isPrintError) {
                log.error("verify token exception", e);
            } else {
                log.debug("verify token exception", e);
            }
        }
        return null;
    }
}
