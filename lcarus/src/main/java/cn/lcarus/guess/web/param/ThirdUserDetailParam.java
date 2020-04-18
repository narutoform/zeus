package cn.lcarus.guess.web.param;

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
@ApiModel(value = "三方用户明细", description = "请求")
public class ThirdUserDetailParam implements Serializable {

    private static final long serialVersionUID = 4708653086894429682L;

    @ApiModelProperty(value = "openId")
    private String openId;

    @ApiModelProperty(value = "会话密钥")
    private String sessionKey;

    @ApiModelProperty(value = "appId")
    private String appId;

    @ApiModelProperty(value = "unionId")
    private String unionId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "语言")
    private String language;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "国家信息")
    private String country;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

}
