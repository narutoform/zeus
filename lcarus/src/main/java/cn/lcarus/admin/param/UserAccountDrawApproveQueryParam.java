package cn.lcarus.admin.param;

import cn.lcarus.common.entity.UserAccountDrawApprove;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author xincheng
 * @date 2019-10-27
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "用户账户提现审核列表查询参数")
public class UserAccountDrawApproveQueryParam extends Page<UserAccountDrawApprove> {

    private static final long serialVersionUID = -964182470760958916L;

    @ApiModelProperty(value = "审批id")
    private Long userAccountDrawApproveId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "申请时间起")
    private Date createTimeStart;

    @ApiModelProperty(value = "申请时间止")
    private Date createTimeEnd;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "状态 0:待审核 1:审核通过 2:审核不通过 UserAccountDrawApproveStateEnum")
    private Integer state;

}
