package cn.lcarus.admin.param;

import cn.lcarus.common.entity.UserAccountDrawApprove;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author xincheng
 * @date 2019-10-27
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "用户账户提现审核提交参数")
public class UserAccountDrawApproveSubmitParam extends Page<UserAccountDrawApprove> {

    private static final long serialVersionUID = -964182470760958916L;

    @ApiModelProperty(value = "审批id")
    @NotNull(message = "审批id不能为空")
    private Long userAccountDrawApproveId;

    @ApiModelProperty(value = "是否通过 true: 通过 false: 不通过")
    @NotNull(message = "是否通过标识不能为空")
    private Boolean passFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

}
