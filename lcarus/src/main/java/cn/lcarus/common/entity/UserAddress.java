package cn.lcarus.common.entity;

import cn.chinotan.zeus.framework.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 用户地址表
 * </p>
 *
 * @author xingcheng
 * @since 2020-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserAddress对象", description = "用户地址表")
public class UserAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "是否删除 1:删除 0:未删除")
    @TableLogic
    private Boolean del;

    @ApiModelProperty(value = "版本号")
    @Version
    private Long version;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
