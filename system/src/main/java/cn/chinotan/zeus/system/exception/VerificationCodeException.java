package cn.chinotan.zeus.system.exception;

import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.exception.ZeusException;

/**
 * 验证码校验异常
 *
 * @author xingcheng
 * @date 2018-11-08
 */
public class VerificationCodeException extends ZeusException {
	private static final long serialVersionUID = -2640690119865434398L;

	public VerificationCodeException(String message) {
        super(message);
    }

    public VerificationCodeException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public VerificationCodeException(ApiCode apiCode) {
        super(apiCode);
    }
}
