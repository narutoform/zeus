package cn.lcarus.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <pre>
 * config 更新参数对象
 * </pre>
 *
 * @author geekidea
 * @date 2019-10-24
 */
@Data
@ApiModel(value = "config 更新参数对象", description = "请求")
public class ConfigUpdateParam implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "key")
    @NotBlank(message = "key不能为空")
    private String key;

    @ApiModelProperty(value = "value")
    @NotBlank(message = "value不能为空")
    private String value;

    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "是否启用 true: 启用 false:禁用")
    private Boolean enable;

}
