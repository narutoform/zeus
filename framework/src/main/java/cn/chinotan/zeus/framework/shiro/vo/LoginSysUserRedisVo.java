package cn.chinotan.zeus.framework.shiro.vo;

import cn.chinotan.zeus.framework.common.bean.ClientInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 登录用户Redis对象，后台使用
 *
 * @author xingcheng
 * @date 2019-9-30
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LoginSysUserRedisVo extends LoginSysUserVo {

    private static final long serialVersionUID = -3858850188055605806L;

    /**
     * 包装后的盐值
     */
    private String salt;

    /**
     * 登录ip
     */
    private ClientInfo clientInfo;

}
