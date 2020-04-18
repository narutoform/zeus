package cn.chinotan.zeus.framework.common.exception;

import cn.chinotan.zeus.framework.common.api.ApiCode;

/**
 * 业务异常
 *
 * @author xingcheng
 * @date 2018-11-08
 */
public class BusinessException extends ZeusException {
	private static final long serialVersionUID = -2303357122330162359L;

	public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(ApiCode apiCode) {
        super(apiCode);
    }

    public BusinessException(Exception e) {
        super(e);
    }

}
