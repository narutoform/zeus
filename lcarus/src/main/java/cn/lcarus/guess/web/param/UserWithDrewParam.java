package cn.lcarus.guess.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @program: guess
 * @description: 用户授权登录请求
 * @author: xingcheng
 * @create: 2019-08-31 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "用户提现请求Param", description = "请求")
public class UserWithDrewParam extends LoginUserVo {
    
    private static final long serialVersionUID = 129032839843309877L;

    @ApiModelProperty("appId")
    @NotBlank(message = "appId不能为空")
    private String appId;
}
