package cn.lcarus.guess.web.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "用户分享后领取红包res", description = "响应")
public class UserShareDrawRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty(value = "分享日 格式yyyy-mm-dd")
    private String shareDay;

    @ApiModelProperty(value = "状态{0:'可领取',1:'已领取'} UserShareRecordStateEnum")
    private Integer state;

    @ApiModelProperty(value = "分享获得金额 单位元")
    private BigDecimal shareAmount;

    @ApiModelProperty("分享次数是否超限制")
    private Boolean shareLimitFlag = false;

}
