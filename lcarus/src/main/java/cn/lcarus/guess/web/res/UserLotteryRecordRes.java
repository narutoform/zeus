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
@ApiModel(value = "当前用户抽奖记录Res", description = "响应")
public class UserLotteryRecordRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;
    
    @ApiModelProperty(value = "抽奖活动主键id")
    private Long lotteryActivityId;

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

    @ApiModelProperty(value = "上架时间")
    private Date upTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "状态{0:'参与抽奖(初始化)',1:'已中奖',2:'未中奖'} UserLotteryRecordStateEnum")
    private Integer userLotteryRecordState;

    @ApiModelProperty(value = "是否领取 false:未领取 true:已领取")
    private Boolean getFlag;

    @ApiModelProperty(value = "中奖类型{0:'红包',1:'实物'} LotteryActivityTypeEnum")
    private Integer winType;

    @ApiModelProperty(value = "中奖时间")
    private Date winTime;
}
