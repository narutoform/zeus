package cn.chinotan.zeus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import cn.chinotan.zeus.framework.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * <pre>
 * 系统权限
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysPermission对象", description = "系统权限")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("路径")
    private String url;

    @ApiModelProperty("唯一编码")
    @NotBlank(message = "唯一编码不能为空")
    private String code;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("类型，1：菜单，2：按钮")
    @NotNull(message = "类型，1：菜单，2：按钮不能为空")
    private Integer type;

    @ApiModelProperty("层级，1：第一级，2：第二级，N：第N级")
    @NotNull(message = "层级，1：第一级，2：第二级，N：第N级不能为空")
    private Integer level;

    @ApiModelProperty("状态，0：禁用，1：启用")
    private Integer state;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("版本")
    @Null(message = "版本不用传")
    @Version
    private Integer version;

    @ApiModelProperty("创建时间")
    @Null(message = "创建时间不用传")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @Null(message = "修改时间不用传")
    private Date updateTime;

}
