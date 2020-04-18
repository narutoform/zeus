package cn.chinotan.zeus.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * 系统用户 查询结果对象
 * </pre>
 *
 * @author xingcheng
 * @date 2019-10-24
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SysUserQueryVo对象", description = "系统用户查询参数")
public class SysUserQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", example = "1")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty(value = "性别，0：女，1：男，默认1", example = "1")
    private Integer gender;

    @ApiModelProperty("头像")
    private String head;

    @ApiModelProperty("remark")
    private String remark;

    @ApiModelProperty(value = "状态，0：禁用，1：启用，2：锁定", example = "1")
    private Integer state;

    @ApiModelProperty(value = "部门id", example = "1")
    private Long departmentId;

    @ApiModelProperty(value = "角色id", example = "1")
    private Long roleId;

    @ApiModelProperty(value = "逻辑删除，0：未删除，1：已删除", example = "1")
    private Integer deleted;

    @ApiModelProperty(value = "版本", example = "1")
    private Integer version;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("角色名称")
    private String roleName;

}
