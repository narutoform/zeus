package cn.lcarus.guess.web.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:24
 **/
@Data
@EqualsAndHashCode()
@ApiModel(value = "用户地址信息列表", description = "响应")
public class UserAddressListRes implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;

    @ApiModelProperty(value = "邮编")
    private String postalCode;

    @ApiModelProperty(value = "国标收货地址第一级地址")
    private String provinceName;

    @ApiModelProperty(value = "国标收货地址第二级地址")
    private String cityName;

    @ApiModelProperty(value = "国标收货地址第三级地址")
    private String countyName;

    @ApiModelProperty(value = "详细收货地址信息")
    private String detailInfo;

    @ApiModelProperty(value = "收货地址国家码")
    private String nationalCode;

    @ApiModelProperty(value = "收货人手机号码")
    private String telNumber;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
