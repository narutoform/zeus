package cn.chinotan.zeus.framework.shiro.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 登录用户对象，响应给前端
 * </p>
 *
 * @author xingcheng
 * @date 2019-05-15
 **/
@Data
@Accessors(chain = true)
public class LoginSysUserVo implements Serializable {

    private static final long serialVersionUID = -1758338570596088158L;

    @ApiModelProperty(value = "主键", example = "1")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty(value = "性别，0：女，1：男，默认1", example = "1")
    private Integer gender;

    @ApiModelProperty(value = "状态，0：禁用，1：启用，2：锁定", example = "1")
    private Integer state;

    @ApiModelProperty(value = "部门id", example = "1")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty(value = "角色id", example = "1")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("权限编码列表")
    private Set<String> permissionCodes;

}
