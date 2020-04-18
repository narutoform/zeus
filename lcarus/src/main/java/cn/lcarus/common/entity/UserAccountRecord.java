package cn.lcarus.common.entity;

import cn.chinotan.zeus.framework.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 账户明细
 * </p>
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserAccountRecord对象", description = "账户明细")
public class UserAccountRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", example = "1")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "支入支出类型{0:'支出',1:'支入'} UserAccountTypeEnum")
    private Integer type;

    @ApiModelProperty(value = "支入支出金额  单位元")
    private BigDecimal amount;

    @ApiModelProperty(value = "使用类型{0:'现金红包',1:'提现'} UserAccountUseTypeEnum")
    private Integer useType;

    @ApiModelProperty(value = "状态{0:'初始化',10:'进行中（需等待）中',20:'进行中（可重试）',30;'成功',40:'最终失败'} 现金红包类型默认成功10 提现默认初始化0 UserAccountRecordStateEnum")
    private Integer state;

    @ApiModelProperty(value = "错误信息")
    private String errMsg;

    @ApiModelProperty(value = "提现渠道{0:'微信红包支付'} UserAccountRecordPayChannelEnum")
    private Integer payChannel;

    @ApiModelProperty(value = "三方单号")
    private String thirdUniqueId;

    @ApiModelProperty(value = "是否删除 1:删除 0:未删除")
    @TableLogic
    private Boolean del;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
