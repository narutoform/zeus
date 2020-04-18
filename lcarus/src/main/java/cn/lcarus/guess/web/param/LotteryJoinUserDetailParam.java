package cn.lcarus.guess.web.param;

import cn.lcarus.common.entity.UserLotteryRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@ApiModel(value = "抽奖活动参与用户明细param", description = "请求")
public class LotteryJoinUserDetailParam extends Page<UserLotteryRecord> {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty("活动id")
    @NotNull(message = "抽奖活动id不能为空")
    private Long lotteryActivityId;
}
