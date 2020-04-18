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
@ApiModel(value = "UserGrantLoginParam对象", description = "用户授权登录请求参数")
public class UserGrantLoginParam extends UseInfoBaseParam {

    private static final long serialVersionUID = 129032839843309877L;

    @ApiModelProperty("微信授权code")
    @NotBlank(message = "微信授权code不能为空")
    private String code;

}
