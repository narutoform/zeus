package cn.lcarus.admin.param;

import cn.lcarus.common.entity.LotteryActivity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author xincheng
 * @date 2019-10-27
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "抽奖活动列表查询参数")
public class LotteryActivityListParam extends Page<LotteryActivity> {

    private static final long serialVersionUID = -964182470760958916L;

    @ApiModelProperty(value = "活动id")
    private Long lotteryActivityId;

    @ApiModelProperty(value = "抽奖活动状态{0:'待开启',1:'待开奖',2:已结束'}")
    private Integer state;

    @ApiModelProperty(value = "开奖时间起")
    private Date openTimeStart;

    @ApiModelProperty(value = "开奖时间止")
    private Date openTimeEnd;

    @ApiModelProperty(value = "奖品名称")
    private String name;

}
