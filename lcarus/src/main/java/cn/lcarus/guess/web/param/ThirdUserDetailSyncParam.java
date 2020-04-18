package cn.lcarus.guess.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "三方用户明细同步请求", description = "请求")
public class ThirdUserDetailSyncParam extends UseInfoBaseParam {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty(value = "userId", hidden = true)
    private Long userId;

}
