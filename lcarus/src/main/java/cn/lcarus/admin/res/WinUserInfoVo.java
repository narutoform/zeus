package cn.lcarus.admin.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xincheng
 * @date 2019-10-24
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "抽奖活动中奖用户信息vo", description = "响应")
public class WinUserInfoVo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "中奖用户id")
    private Long userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "中奖用户收货地址")
    private String userAddress;

    @ApiModelProperty(value = "中奖用户收货人电话")
    private String telNumber;

    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;
}
