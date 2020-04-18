package cn.lcarus.admin.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xincheng
 * @date 2019-10-24
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ecpm列表查询vo", description = "响应")
public class EcpmListVo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "触发日期 日期格式 yyyy-MM-dd")
    private String triggerDay;

    @ApiModelProperty(value = "状态{0:'待生效',1:'已生效'}")
    private Integer state;

    @ApiModelProperty(value = "ecpm值")
    private BigDecimal ecpm;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
