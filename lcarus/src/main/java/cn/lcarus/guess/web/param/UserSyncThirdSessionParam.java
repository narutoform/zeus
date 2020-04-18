package cn.lcarus.guess.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2019-08-31 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "登录同步三方session接口对象", description = "请求参数")
public class UserSyncThirdSessionParam extends LoginUserVo {

    private static final long serialVersionUID = 129032839843309877L;

    @ApiModelProperty("授权code")
    @NotBlank(message = "授权code不能为空")
    private String code;

    @ApiModelProperty("appId")
    @NotBlank(message = "appId不能为空")
    private String appId;

}
