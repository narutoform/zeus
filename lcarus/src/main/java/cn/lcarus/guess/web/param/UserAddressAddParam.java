package cn.lcarus.guess.web.param;

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
@ApiModel(value = "用户添加地址参数param", description = "请求")
public class UserAddressAddParam extends LoginUserVo {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty(value = "用户收货地址id 添加时不传，编辑时传")
    private Long userAddressId;

    @ApiModelProperty(value = "收货人姓名")
    @NotBlank(message = "收货人姓名不能为空")
    private String consigneeName;

    @ApiModelProperty(value = "邮编")
    private String postalCode;

    @ApiModelProperty(value = "国标收货地址第一级地址")
    @NotBlank(message = "省地址不能为空")
    private String provinceName;

    @ApiModelProperty(value = "国标收货地址第二级地址")
    @NotBlank(message = "城市地址不能为空")
    private String cityName;

    @ApiModelProperty(value = "国标收货地址第三级地址")
    @NotBlank(message = "乡镇区域地址不能为空")
    private String countyName;

    @ApiModelProperty(value = "详细收货地址信息")
    @NotBlank(message = "乡镇地址不能为空")
    private String detailInfo;

    @ApiModelProperty(value = "收货地址国家码")
    private String nationalCode;

    @ApiModelProperty(value = "收货人手机号码")
    @NotBlank(message = "收货人手机号码不能为空")
    private String telNumber;

}
