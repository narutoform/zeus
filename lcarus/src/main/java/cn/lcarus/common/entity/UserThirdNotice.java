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
 * 用户三方订阅消息记录
 * </p>
 *
 * @author xingcheng
 * @since 2020-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserThirdNotice对象", description = "用户三方订阅消息记录")
public class UserThirdNotice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "openId")
    private String openId;

    @ApiModelProperty(value = "appId")
    private String appId;

    @ApiModelProperty(value = "模板id")
    private String templateId;

    @ApiModelProperty(value = "可发送次数")
    private Long enableSendNum;

    @ApiModelProperty(value = "版本号")
    @Version
    private Long version;

    @ApiModelProperty(value = "是否删除 1:删除 0:未删除")
    @TableLogic
    private Boolean del;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
