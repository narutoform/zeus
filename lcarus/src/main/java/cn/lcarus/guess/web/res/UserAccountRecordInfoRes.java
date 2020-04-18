package cn.lcarus.guess.web.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:24
 **/
@Data
@EqualsAndHashCode
@ApiModel(value = "用户账户收支展示res", description = "响应")
public class UserAccountRecordInfoRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty(value = "用户id", example = "1")
    private Long userId;

    @ApiModelProperty(value = "支入支出类型{0:'支出',1:'支入'} UserAccountTypeEnum", example = "1")
    private Integer type;

    @ApiModelProperty(value = "支入支出金额  单位元")
    private BigDecimal amount;

    @ApiModelProperty(value = "使用类型{0:'现金红包',1:'提现'} UserAccountUseTypeEnum", example = "1")
    private Integer useType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    @ApiModelProperty(value = "状态{0:'初始化',10:'进行中（需等待）中',20:'进行中（可重试）',30;'成功',40:'最终失败'} 现金红包类型默认成功10 提现默认初始化0 UserAccountRecordStateEnum", example = "1")
    private Integer state;

}
