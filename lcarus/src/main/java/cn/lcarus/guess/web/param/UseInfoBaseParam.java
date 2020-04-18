package cn.lcarus.guess.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: guess
 * @description: 用户授权登录请求
 * @author: xingcheng
 * @create: 2019-08-31 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "UseInfoBaseParam对象", description = "用户授权登录基本参数")
public class UseInfoBaseParam implements Serializable {
    
    private static final long serialVersionUID = 129032839843309877L;

    @ApiModelProperty("appId")
    @NotBlank(message = "appId不能为空")
    private String appId;

    @ApiModelProperty("encryptedData 小程序使用")
    private String encryptedData;

    @ApiModelProperty("iv 小程序使用")
    private String iv;

    @ApiModelProperty("signature 小程序使用")
    private String signature;

    @ApiModelProperty("rawData 小程序使用")
    private String rawData;
}
