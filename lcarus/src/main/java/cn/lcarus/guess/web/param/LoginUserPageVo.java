package cn.lcarus.guess.web.param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: guess
 * @description: 用户登录信息
 * @author: xingcheng
 * @create: 2019-08-31 20:03
 **/
@Data
@ApiModel("用户登录信息")
public class LoginUserPageVo<T> extends Page<T> {

    private static final long serialVersionUID = 8020439303953242420L;

    @ApiModelProperty("用户id")
    private Long userId;
}
