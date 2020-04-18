package cn.lcarus.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
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
@ApiModel(value = "抽奖活动添加参数")
public class LotteryActivityAddParam implements Serializable {

    private static final long serialVersionUID = -964182470760958916L;

    @ApiModelProperty(value = "奖品名称")
    @NotBlank(message = "奖品名称不能为空")
    private String name;

    @ApiModelProperty(value = "奖品数量")
    @NotNull(message = "奖品数量不能为空")
    private Integer num;

    @ApiModelProperty(value = "单个奖品成本 实物奖品必填")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "奖品主图地址")
    @NotBlank(message = "奖品主图地址不能为空")
    private String mainImageUrl;

    @ApiModelProperty(value = "奖品详情图地址")
    @NotBlank(message = "奖品详情图地址不能为空")
    private String detailImageUrl;

    @ApiModelProperty(value = "奖品类型{0:'红包',1:'实物'} LotteryActivityTypeEnum")
    @NotNull(message = "奖品类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "开奖时间")
    @NotNull(message = "开奖时间不能为空")
    private Date openTime;

}
