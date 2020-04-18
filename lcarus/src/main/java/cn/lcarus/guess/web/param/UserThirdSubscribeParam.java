package cn.lcarus.guess.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @program: guess
 * @description: 用户授权登录请求
 * @author: xingcheng
 * @create: 2019-08-31 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "用户订阅结果请求Param", description = "请求")
public class UserThirdSubscribeParam extends LoginUserVo {

    private static final long serialVersionUID = 129032839843309877L;

    @ApiModelProperty("appId 小程序")
    @NotBlank(message = "appId不能为空")
    private String appId;

    @ApiModelProperty("订阅结果")
    @NotEmpty(message = "订阅结果不能为空")
    @Valid
    private List<SubscribeResultParam> subscribeResultList;
}
