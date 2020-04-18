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
 * 用户抽奖记录表
 * </p>
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true, of = {"userId", "lotteryActivityId"})
@ApiModel(value = "UserLotteryRecord对象", description = "用户抽奖记录表")
public class UserLotteryRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "抽奖活动id")
    private Long lotteryActivityId;

    @ApiModelProperty(value = "状态{0:'参与抽奖(初始化)',1:'已中奖',2:'未中奖'} UserLotteryRecordStateEnum")
    private Integer state;

    @ApiModelProperty(value = "是否领取 false:未领取 true:已领取")
    private Boolean getFlag;

    @ApiModelProperty(value = "中奖金额 当活动是红包类型才有效 单位元")
    private BigDecimal winAmount;

    @ApiModelProperty(value = "用户账户记录id 当活动是红包类型才有效")
    private Long userAccountRecordId;

    @ApiModelProperty(value = "中奖类型{0:'红包',1:'实物'} LotteryActivityTypeEnum")
    private Integer winType;

    @ApiModelProperty(value = "是否机器人参与抽奖")
    private Boolean robotFlag;

    @ApiModelProperty(value = "用户收货地址id 实物奖品有该值")
    private Long userAddressId;

    @ApiModelProperty(value = "中奖时间")
    private Date winTime;

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
