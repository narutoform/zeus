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
@ApiModel(value = "用户账户提现审核列表查询vo", description = "响应")
public class UseAccountDrawApproveVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "userAccountDrawApproveId")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "提现金额 元")
    private BigDecimal drawAccountAmount;
    
    @ApiModelProperty(value = "用户账户记录id")
    private Long userAccountRecordId;

    @ApiModelProperty(value = "状态 0:待审核 1:审核通过 2:审核不通过 UserAccountDrawApproveStateEnum")
    private Integer state;

    @ApiModelProperty(value = "审核时间")
    private Date approveTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "申请时间")
    private Date createTime;
}
