package cn.chinotan.zeus.system.param;

import cn.chinotan.zeus.framework.core.pagination.BasePageOrderParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <pre>
 * 系统权限 查询参数对象
 * </pre>
 *
 * @author xingcheng
 * @date 2019-10-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysPermissionPageParam对象", description = "系统权限查询参数")
public class SysPermissionPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
