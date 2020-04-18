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
@ApiModel(value = "用户账户信息展示res", description = "响应")
public class UserAccountInfoRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty(value = "用户余额")
    private BigDecimal amount;

    @ApiModelProperty(value = "每次提现的最低限 单位元")
    private BigDecimal limitAmountDown;

}
