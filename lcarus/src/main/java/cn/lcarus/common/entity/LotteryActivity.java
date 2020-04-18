package cn.lcarus.common.entity;

import cn.chinotan.zeus.framework.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 抽奖活动
 * </p>
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LotteryActivity对象", description = "抽奖活动")
public class LotteryActivity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "奖品名称")
    private String name;

    @ApiModelProperty(value = "抽奖活动状态{0:'待开启',1:'待开奖',2:已结束'} LotteryActivityStateEnum ")
    private Integer state;

    @ApiModelProperty(value = "奖品数量")
    private Integer num;

    @ApiModelProperty(value = "单个奖品成本")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "奖品主图地址")
    private String mainImageUrl;

    @ApiModelProperty(value = "奖品详情图地址")
    private String detailImageUrl;

    @ApiModelProperty(value = "奖品类型{0:'红包',1:'实物'} LotteryActivityTypeEnum")
    private Integer type;

    @ApiModelProperty(value = "奖品状态{0:'下架',1:'上架'} LotteryActivityProductStateEnum")
    private Integer productState;

    @ApiModelProperty(value = "开奖时间")
    private Date openTime;

    @ApiModelProperty(value = "上架时间")
    private Date upTime;

    @ApiModelProperty(value = "版本号")
    @Version
    private Integer version;

    @ApiModelProperty(value = "是否删除 1:删除 0:未删除")
    @TableLogic
    private Boolean del;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
