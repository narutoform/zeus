package cn.chinotan.zeus.system.param;

import cn.chinotan.zeus.framework.shiro.service.LoginUsername;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录参数
 *
 * @author xingcheng
 * @date 2019-05-15
 **/
@Data
@ApiModel("登录参数")
public class LoginParam implements LoginUsername {
	private static final long serialVersionUID = 2854217576695117356L;

	@NotBlank(message = "请输入账号")
    @ApiModelProperty(value = "账号")
    private String username;

    @NotBlank(message = "请输入密码")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty("验证码Token")
    private String verifyToken;

    @ApiModelProperty("验证码")
    private String code;

}
