package cn.chinotan.zeus.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author geekidea
 * @date 2018-11-08
 */
@ApiModel("oss响应")
@Data
@NoArgsConstructor
public class OssPathvo implements Serializable {
    
    private static final long serialVersionUID = -1683800405530086022L;

    @ApiModelProperty("相对地址")
    private String relativeAddress;

    @ApiModelProperty("绝对地址")
    private String absoluteAddress;

    public OssPathvo(String relativeAddress, String absoluteAddress) {
        this.relativeAddress = relativeAddress;
        this.absoluteAddress = absoluteAddress;
    }
}
