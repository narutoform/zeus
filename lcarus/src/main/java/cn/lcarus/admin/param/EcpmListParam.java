package cn.lcarus.admin.param;

import cn.lcarus.common.entity.Ecpm;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xincheng
 * @date 2019-10-27
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "ecpm查询参数")
public class EcpmListParam extends Page<Ecpm> {

    private static final long serialVersionUID = -964182470760958916L;

    @ApiModelProperty(value = "ecpmId")
    private Long ecpmId;

    @ApiModelProperty(value = "时间起")
    private Date dateStart;

    @ApiModelProperty(value = "时间止")
    private Date dateEnd;

    @ApiModelProperty(value = "ecpm值")
    private BigDecimal ecpm;

}
