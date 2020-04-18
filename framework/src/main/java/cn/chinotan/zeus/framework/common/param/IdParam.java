package cn.chinotan.zeus.framework.common.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ID参数
 *
 * @author xingcheng
 * @date 2018-11-08
 */
@Data
@ApiModel("ID参数")
public class IdParam implements Serializable {
    private static final long serialVersionUID = -5353973980674510450L;

    @NotNull(message = "ID不能为空")
    private Long id;

}
