package cn.lcarus.guess.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: guess
 * @description: 用户登录信息
 * @author: xingcheng
 * @create: 2019-08-31 20:03
 **/
@Data
@ApiModel("用户登录信息")
public class LoginUserVo implements Serializable {
    
    private static final long serialVersionUID = -7132137502528693726L;
    
    @ApiModelProperty(value = "用户id", hidden = true)
    private Long userId;
}
