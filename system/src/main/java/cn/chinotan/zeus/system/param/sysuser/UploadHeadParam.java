package cn.chinotan.zeus.system.param.sysuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 上传头像参数
 *
 * @author xingcheng
 * @date 2020/3/7
 **/
@Data
@Accessors(chain = true)
@ApiModel("上传头像参数")
public class UploadHeadParam implements Serializable {

    private static final long serialVersionUID = -6871175837435010592L;

    @ApiModelProperty(value = "用户ID", example = "1")
    @NotNull(message = "用户ID不能为空")
    private Long id;

    @ApiModelProperty("头像路径")
    @NotBlank(message = "头像不能为空")
    private String head;

}
