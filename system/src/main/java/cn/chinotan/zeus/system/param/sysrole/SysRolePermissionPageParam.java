package cn.chinotan.zeus.system.param.sysrole;

import cn.chinotan.zeus.framework.core.pagination.BasePageOrderParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <pre>
 * 角色权限关系 查询参数对象
 * </pre>
 *
 * @author xingcheng
 * @date 2019-10-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysRolePermissionPageParam对象", description = "角色权限关系查询参数")
public class SysRolePermissionPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
