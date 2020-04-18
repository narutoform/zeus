package cn.chinotan.zeus.framework.front.login.service;

/**
 * <p>
 * 登录服务接口
 * </p>
 *
 * @author xingcheng
 * @date 2019-08-17
 **/
public interface BaseLoginService {

    /**
     * 登录
     *
     * @param userId
     * @return
     */
    String login(Long userId);

}
