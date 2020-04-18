package cn.lcarus.guess.web.res;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@ApiModel(value = "抽奖首页", description = "响应")
public class LotteryActivityIndexRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty(value = "是否新人")
    private Boolean newUserFlag = true;

    @ApiModelProperty(value = "分享限制 true代表当日不可再次分享领奖励")
    private Boolean shareLimitFlag = false;

    @ApiModelProperty(value = "是否显示提现按钮")
    private Boolean withDrawShowFlag = true;

    @ApiModelProperty(value = "账户余额 单位元")
    private BigDecimal accountAmount;

    @ApiModelProperty(value = "每次提现的最低限 单位元")
    private BigDecimal limitAmountDown;

    @ApiModelProperty(value = "活动列表信息")
    private Page<LotteryActivityListRes> lotteryActivityList;
}
