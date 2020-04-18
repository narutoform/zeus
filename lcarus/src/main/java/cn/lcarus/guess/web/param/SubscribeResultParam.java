package cn.lcarus.guess.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: guess
 * @description: 用户授权登录请求
 * @author: xingcheng
 * @create: 2019-08-31 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "订阅结果Param", description = "请求")
public class SubscribeResultParam implements Serializable {

    private static final long serialVersionUID = 129032839843309877L;

    @ApiModelProperty("templateId")
    @NotBlank(message = "templateId不能为空")
    private String templateId;

    @ApiModelProperty("订阅结果 0:reject 1:accept 2:ban")
    @NotNull(message = "订阅结果不能为空")
    private Integer state;
}
