package cn.chinotan.zeus.framework.common.api;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * REST API 返回结果
 * </p>
 *
 * @author xingcheng
 * @since 2018-11-08
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class ApiResult<T> implements Serializable {
	private static final long serialVersionUID = 8004487252556526569L;
    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time = new Date();

    public ApiResult() {

    }

    public static <T> ApiResult<T> result(boolean flag) {
        if (flag) {
            return ok();
        }
        return fail();
    }

    public static <T> ApiResult<T> result(ApiCode apiCode) {
        return result(apiCode, null);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode, Object data) {
        return result(apiCode, null, data);
    }

    public static <T> ApiResult result(ApiCode apiCode, String msg, Object data) {
        boolean success = false;
        if (apiCode.getCode() == ApiCode.SUCCESS.getCode()) {
            success = true;
        }
        String message = apiCode.getMessage();
        if (StringUtils.isNotBlank(msg)) {
            message = msg;
        }
        return ApiResult.builder()
                .code(apiCode.getCode())
                .message(message)
                .data(data)
                .success(success)
                .time(new Date())
                .build();
    }

    public static ApiResult result(Integer apiCode, String msg, Object data) {
        ApiResult<Object> result = new ApiResult<>();
        result.setCode(apiCode);
        result.setMessage(msg);
        result.setData(data);
        result.setTime(new Date());
        return result;
    }

    public static <T> ApiResult<T> ok() {
        return ok(null);
    }

    public static <T> ApiResult<T> ok(Object data) {
        return result(ApiCode.SUCCESS, data);
    }

    public static <T> ApiResult<T> ok(Object data, String msg) {
        return result(ApiCode.SUCCESS, msg, data);
    }

    public static <T> ApiResult<T> okMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return ok(map);
    }

    public static <T> ApiResult<T> fail(ApiCode apiCode) {
        return result(apiCode, null);
    }

    public static <T> ApiResult<T> fail(ApiResult apiResult) {
        return result(apiResult.getCode(), apiResult.getMessage(), null);
    }

    public static <T> ApiResult<T> fail(String message) {
        return result(ApiCode.FAIL, message, null);
    }

    public static <T> ApiResult<T> fail(ApiCode apiCode, Object data) {
        if (ApiCode.SUCCESS == apiCode) {
            throw new RuntimeException("失败结果状态码不能为" + ApiCode.SUCCESS.getCode());
        }
        return result(apiCode, data);

    }

    public static <T> ApiResult<T> fail(ApiCode apiCode, String msg) {
        return result(apiCode, StrUtil.isBlank(msg) ? apiCode.getMessage() : msg, null);

    }

    public static <T> ApiResult<T> fail(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return result(ApiCode.FAIL, map);
    }

    public static <T> ApiResult<T> fail(Integer code, String msg) {
        return result(code, msg, null);
    }

    public static <T> ApiResult<T> fail() {
        return fail(ApiCode.FAIL);
    }

    public boolean isOk() {
        return code == ApiCode.SUCCESS.getCode();
    }

    public boolean isFail() {
        return code != ApiCode.SUCCESS.getCode();
    }
}
