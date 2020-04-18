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
 * 系统操作日志
 *
 * @author xingcheng
 * @since 2020-03-19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysOperationLog对象")
public class SysOperationLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空", groups = {Update.class})
    private Long id;

    @ApiModelProperty("请求ID")
    private String requestId;

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("日志名称")
    private String name;

    @ApiModelProperty("IP")
    private String ip;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("运营商")
    private String operator;

    @ApiModelProperty("全路径")
    private String path;

    @ApiModelProperty("模块名称")
    private String module;

    @ApiModelProperty("类名")
    private String className;

    @ApiModelProperty("方法名称")
    private String methodName;

    @ApiModelProperty("请求方式，GET/POST")
    private String requestMethod;

    @ApiModelProperty("内容类型")
    private String contentType;

    @ApiModelProperty("是否是JSON请求映射参数")
    private Boolean requestBody;

    @ApiModelProperty("请求参数")
    private String param;

    @ApiModelProperty("tokenMd5值")
    private String token;

    @ApiModelProperty(value = "0:其它,1:新增,2:修改,3:删除,4:详情查询,5:所有列表,6:分页列表,7:其它查询,8:上传文件", example = "1")
    private Integer type;

    @ApiModelProperty(value = "0:失败,1:成功", example = "true")
    private Boolean success;

    @ApiModelProperty(value = "响应结果状态码", example = "1")
    private Integer code;

    @ApiModelProperty("响应结果消息")
    private String message;

    @ApiModelProperty("异常类名称")
    private String exceptionName;

    @ApiModelProperty("异常信息")
    private String exceptionMessage;

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
