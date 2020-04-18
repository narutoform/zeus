package cn.chinotan.zeus.system.param.sysuser;

import cn.chinotan.zeus.framework.core.pagination.BasePageOrderParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <pre>
 * 系统用户 查询参数对象
 * </pre>
 *
 * @author xingcheng
 * @date 2019-10-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysUserPageParam对象", description = "系统用户查询参数")
public class SysUserPageParam extends BasePageOrderParam {

    private static final long serialVersionUID = 7437881671144580610L;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("状态，0：禁用，1：启用，2：锁定")
    private Integer state;

    @ApiModelProperty("创建时间开始")
    private Date createTimeStart;

    @ApiModelProperty("创建时间结束")
    private Date createTimeEnd;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

}
