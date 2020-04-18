package cn.lcarus.guess.web.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "用户分享res", description = "响应")
public class UserShareResultRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty("用户分享记录请求id 当shareLimitFlag为false的时候才有值 已弃用")
    @Deprecated
    private Long userShareRecordId;

    @ApiModelProperty("分享次数是否超限制")
    private Boolean shareLimitFlag = false;

    @ApiModelProperty(value = "广告id")
    private String adUnitId;
}
