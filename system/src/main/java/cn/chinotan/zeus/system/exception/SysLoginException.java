package cn.chinotan.zeus.system.exception;

import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.exception.ZeusException;

/**
 * 系统登录异常
 *
 * @author xingcheng
 * @date 2019-08-04
 */
public class SysLoginException extends ZeusException {
	private static final long serialVersionUID = -3157438982569715170L;

	public SysLoginException(String message) {
        super(message);
    }

    public SysLoginException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public SysLoginException(ApiCode apiCode) {
        super(apiCode);
    }
}
