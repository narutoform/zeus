package cn.lcarus.guess.web.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "抽奖首页活动商品", description = "响应")
public class LotteryActivityListRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "奖品名称")
    private String name;

    @ApiModelProperty(value = "抽奖活动状态{0:'待开启',1:'待开奖',2:已结束'}")
    private Integer state;

    @ApiModelProperty(value = "商品数量")
    private Integer num;

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

    @ApiModelProperty(value = "开奖剩余时间 毫秒")
    private Long openSurplusTime;

    @ApiModelProperty(value = "上架时间")
    private Date upTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否参与")
    private Boolean joinFlag = false;
}
