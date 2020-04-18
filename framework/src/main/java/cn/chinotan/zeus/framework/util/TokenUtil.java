package cn.chinotan.zeus.framework.util;

/**
 * @author xingcheng
 * @date 2018-11-08
 */
public class TokenUtil {

    /**
     *
     * @return
     */
    public static String generateFirstLoginRestPwdToken(){
        String token = "first-login-rest-pwd-token:" + UUIDUtil.getUuid();
        return token;
    }

    /**
     * 生成验证码token
     * @return
     */
    public static String generateVerificationCodeToken(){
        String token = "verification-code-token:" + UUIDUtil.getUuid();
        return token;
    }
}
