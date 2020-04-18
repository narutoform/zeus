package cn.lcarus.guess.web.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "参与抽奖res", description = "响应")
public class LotteryActivityJoinRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty("活动id")
    private Long lotteryActivityId;

    @ApiModelProperty(value = "奖品名称")
    private String name;

    @ApiModelProperty(value = "开奖时间")
    private Date openTime;

}
