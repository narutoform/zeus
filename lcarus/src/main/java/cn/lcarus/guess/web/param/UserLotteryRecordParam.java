package cn.lcarus.guess.web.param;

import cn.lcarus.common.entity.UserLotteryRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "抽奖活动当前用户中奖明细param", description = "请求")
public class UserLotteryRecordParam extends LoginUserPageVo<UserLotteryRecord> {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty("查询类型 WIN: 中奖，JOIN: 参与")
    @NotBlank(message = "查询类型不能为空")
    private String queryType;
}
