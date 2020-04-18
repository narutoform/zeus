package cn.lcarus.admin.param;

import cn.lcarus.common.entity.User;
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
@ApiModel(value = "用户列表查询参数")
public class UserQueryParam extends Page<User> {

    private static final long serialVersionUID = -964182470760958916L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "注册时间起")
    private Date regStart;

    @ApiModelProperty(value = "注册时间止")
    private Date regEnd;

    @ApiModelProperty(value = "手机号")
    private String mobile;

}
