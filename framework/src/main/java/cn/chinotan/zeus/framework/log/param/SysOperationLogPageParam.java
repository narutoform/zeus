package cn.chinotan.zeus.framework.log.param;

import cn.chinotan.zeus.framework.core.pagination.BasePageOrderParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <pre>
 * 系统操作日志 分页参数对象
 * </pre>
 *
 * @author xingcheng
 * @date 2020-03-19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统操作日志分页参数")
public class SysOperationLogPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
