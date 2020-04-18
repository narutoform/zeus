package cn.chinotan.zeus.framework.common.exception;

import cn.chinotan.zeus.framework.common.api.ApiCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 * @author xingcheng
 * @date 2018-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ZeusException extends RuntimeException{

    private static final long serialVersionUID = -2470461654663264392L;

    private Integer errorCode;
    private String message;

    public ZeusException() {
        super();
    }

    public ZeusException(String message) {
        super(message);
        this.message = message;
    }

    public ZeusException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ZeusException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.errorCode = apiCode.getCode();
        this.message = apiCode.getMessage();
    }

    public ZeusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZeusException(Throwable cause) {
        super(cause);
    }

}
