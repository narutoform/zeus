package cn.chinotan.zeus.system.param.sysuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 管理员重置用户密码参数
 *
 * @author xingcheng
 * @date 2020-3-8
 **/
@Data
@Accessors(chain = true)
@ApiModel("管理员重置用户密码参数")
public class ResetPasswordParam implements Serializable {

    private static final long serialVersionUID = 5364321420976152005L;

    @ApiModelProperty("用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "确认密码不能为空")
    private String confirmPassword;

}
