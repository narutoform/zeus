package cn.lcarus.guess.web.res;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@ApiModel(value = "中奖用户明细res", description = "响应")
public class LotteryWinUserDetailRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty("其余中奖用户分页信息")
    private Page<UserShortInfoRes> otherWinUserPage = new Page<>();

    @ApiModelProperty("当前中奖用户信息")
    private UserShortInfoRes currentWinUser;

}
