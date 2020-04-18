package cn.chinotan.zeus.framework.log.entity;

import cn.chinotan.zeus.framework.common.entity.BaseEntity;
import cn.chinotan.zeus.framework.core.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 系统登录日志
 *
 * @author xingcheng
 * @since 2020-03-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysLoginLog对象")
public class SysLoginLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty(value = "主键", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "请求ID", example = "1")
    private String requestId;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("IP")
    private String ip;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("运营商")
    private String operator;

    @ApiModelProperty("tokenMd5值")
    private String token;

    @ApiModelProperty(value = "1:登录，2：登出", example = "1")
    private Integer type;

    @ApiModelProperty(value = "是否成功 true:成功/false:失败", example = "true")
    private Boolean success;

    @ApiModelProperty(value = "响应码", example = "200")
    private Integer code;

    @ApiModelProperty("失败消息记录")
    private String exceptionMessage;

    @ApiModelProperty("浏览器名称")
    private String userAgent;

    @ApiModelProperty("浏览器名称")
    private String browserName;

    @ApiModelProperty("浏览器版本")
    private String browserVersion;

    @ApiModelProperty("浏览器引擎名称")
    private String engineName;

    @ApiModelProperty("浏览器引擎版本")
    private String engineVersion;

    @ApiModelProperty("系统名称")
    private String osName;

    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty(value = "是否是手机,0:否,1:是", example = "true")
    private Boolean mobile;

    @ApiModelProperty("移动端设备名称")
    private String deviceName;

    @ApiModelProperty("移动端设备型号")
    private String deviceModel;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}
