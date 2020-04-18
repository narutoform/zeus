package cn.chinotan.zeus.framework.shiro.service;

import java.io.Serializable;

/**
 * 获取登录token
 *
 * @author xingcheng
 * @date 2020/3/25
 **/
public interface LoginToken extends Serializable {

    /**
     * 获取登录token
     *
     * @return
     */
    String getToken();

}
