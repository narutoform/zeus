package cn.lcarus.admin.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xincheng
 * @date 2019-10-24
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "抽奖活动vo", description = "响应")
public class LotteryActivityListVo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "奖品名称")
    private String name;

    @ApiModelProperty(value = "抽奖活动状态{0:'待开启',1:'待开奖',2:已结束'}")
    private Integer state;

    @ApiModelProperty(value = "奖品数量")
    private Integer num;

    @ApiModelProperty(value = "单个奖品成本")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "奖品主图地址")
    private String mainImageUrl;

    @ApiModelProperty(value = "奖品详情图地址")
    private String detailImageUrl;

    @ApiModelProperty(value = "奖品类型{0:'红包',1:'实物'}")
    private Integer type;

    @ApiModelProperty(value = "奖品状态{0:'下架',1:'上架'}")
    private Integer productState;

    @ApiModelProperty(value = "开奖时间")
    private Date openTime;

    @ApiModelProperty(value = "上架时间")
    private Date upTime;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "抽奖参与人数，不含机器人")
    private Integer joinAllHumanNum;

    @ApiModelProperty(value = "抽奖参与人数，含机器人")
    private Integer joinAllNum;

    @ApiModelProperty(value = "是否真人")
    private Boolean humanFlag = true;

    @ApiModelProperty(value = "中奖用户信息 只有实物中奖用户才有")
    private List<WinUserInfoVo> winUserInfoVos;
}
