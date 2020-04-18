package cn.chinotan.zeus.framework.shiro.service;

import java.io.Serializable;

/**
 * 获取登录用户名称
 *
 * @author xingcheng
 * @date 2020/3/24
 **/
public interface LoginUsername extends Serializable {

    /**
     * 获取用户名
     *
     * @return
     */
    String getUsername();

}
