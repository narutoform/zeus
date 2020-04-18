package cn.lcarus.admin.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xincheng
 * @date 2019-10-24
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "用户信息vo", description = "响应")
public class UserInfoVo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "参与抽奖数")
    private Integer joinNum;

    @ApiModelProperty(value = "中奖记录数")
    private Integer wimNum;

    @ApiModelProperty(value = "累计红包余额")
    private BigDecimal accountAmount;

    @ApiModelProperty(value = "已提现金额")
    private BigDecimal withDrewAmount;
}
