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

import java.util.Date;

/**
 * <p>
 * 用户提现审核表
 * </p>
 *
 * @author xingcheng
 * @since 2020-04-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserAccountDrawApprove对象", description = "用户提现审核表")
public class UserAccountDrawApprove extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户账户记录id")
    private Long userAccountRecordId;

    @ApiModelProperty(value = "状态 0:待审核 1:审核通过 2:审核不通过 UserAccountDrawApproveStateEnum")
    private Integer state;

    @ApiModelProperty(value = "审核时间")
    private Date approveTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "版本号")
    @Version
    private Long version;

    @ApiModelProperty(value = "是否删除 1:删除 0:未删除")
    @TableLogic
    private Boolean del;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
