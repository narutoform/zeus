package cn.lcarus.guess.web.res;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: guess
 * @description: 用户登录响应
 * @author: xingcheng
 * @create: 2020-03-28 13:48
 **/
@Data
public class UserLoginRes implements Serializable {

    private static final long serialVersionUID = 4577710258107419415L;

    /**
     * token
     */
    private String token;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * appId
     */
    private String appId;
}
