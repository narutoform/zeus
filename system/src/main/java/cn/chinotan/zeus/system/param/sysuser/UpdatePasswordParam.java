package cn.chinotan.zeus.system.param.sysuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改密码参数
 *
 * @author xingcheng
 * @date 2019-10-27
 **/
@Data
@Accessors(chain = true)
@ApiModel("修改密码参数")
public class UpdatePasswordParam implements Serializable {

    private static final long serialVersionUID = -186284285725426339L;

    @ApiModelProperty(value = "用户id", example = "1")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty("原密码")
    @NotEmpty(message = "原密码不能为空")
    private String oldPassword;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "确认密码不能为空")
    private String confirmPassword;

}
