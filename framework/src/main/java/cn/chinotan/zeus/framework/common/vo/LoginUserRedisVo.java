package cn.chinotan.zeus.framework.common.vo;

import cn.chinotan.zeus.framework.common.bean.ClientInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *  登录用户redis对象，front使用
 * </p>
 * @author xingcheng
 * @date 2019-08-17
 **/
@Data
@ApiModel("front用户登录值对象")
public class LoginUserRedisVo implements Serializable {

    private static final long serialVersionUID = -4497185071769175695L;

    /**
     * 登录用户id
     */
    private Long userId;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 登录ip
     */
    private ClientInfo clientInfo;

    /**
     * 登录token
     */
    private String token;

    /**
     * 创建时间
     */
    private Date createDate;

}
