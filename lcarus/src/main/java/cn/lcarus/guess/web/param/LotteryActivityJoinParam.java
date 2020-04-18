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
@ApiModel(value = "参与抽奖param", description = "请求")
public class LotteryActivityJoinParam extends LoginUserVo {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty("活动id")
    @NotNull(message = "抽奖活动id不能为空")
    private Long lotteryActivityId;
}
