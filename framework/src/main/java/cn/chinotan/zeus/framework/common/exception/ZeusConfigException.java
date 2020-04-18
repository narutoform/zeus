package cn.chinotan.zeus.framework.common.exception;

import cn.chinotan.zeus.framework.common.api.ApiCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * zeus配置异常
 *
 * @author xingcheng
 * @date 2020/3/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ZeusConfigException extends ZeusException {

    private static final long serialVersionUID = 8952028631871769425L;

    private Integer errorCode;
    private String message;

    public ZeusConfigException() {
        super();
    }

    public ZeusConfigException(String message) {
        super(message);
        this.message = message;
    }

    public ZeusConfigException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ZeusConfigException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.errorCode = apiCode.getCode();
        this.message = apiCode.getMessage();
    }

    public ZeusConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZeusConfigException(Throwable cause) {
        super(cause);
    }

}
