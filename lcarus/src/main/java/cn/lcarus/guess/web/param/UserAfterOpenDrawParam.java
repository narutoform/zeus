package cn.lcarus.guess.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "用户开奖后领取奖品param", description = "请求")
public class UserAfterOpenDrawParam extends LoginUserVo {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty("用户参与抽奖请求id")
    @NotNull(message = "用户参与抽奖请求id不能为空")
    private Long userLotteryRecordId;

    @ApiModelProperty("用户收货地址id 实物类抽奖并且中奖后传递该值")
    private Long userAddressId;
}
