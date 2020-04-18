package cn.lcarus.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xincheng
 * @date 2019-10-27
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "ecpm添加参数")
public class EcpmAddParam implements Serializable {

    private static final long serialVersionUID = -964182470760958916L;

    @ApiModelProperty(value = "生效日期")
    @NotNull(message = "生效日期不能为空")
    private Date triggerDay;

    @ApiModelProperty(value = "ecpm值")
    @NotNull(message = "ecpm值不能为空")
    private BigDecimal ecpm;

}
