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
 * 用户分享记录表
 * </p>
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserShereRecord对象", description = "用户分享记录表")
public class UserShareRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "分享日 格式yyyy-mm-dd")
    private String shareDay;

    @ApiModelProperty(value = "状态{1:'可领取',1:'已领取'} UserShareRecordStateEnum")
    private Integer state;

    @ApiModelProperty(value = "分享获得金额 单位元")
    private BigDecimal shareAmount;

    @ApiModelProperty(value = "用户账户记录id")
    private Long userAccountRecordId;

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
