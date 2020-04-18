package cn.chinotan.zeus.framework.shiro.exception;

import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.exception.ZeusException;

/**
 * Shiro配置异常
 *
 * @author xingcheng
 * @date 2019-09-29
 * @since 1.3.0.RELEASE
 **/
public class ShiroConfigException extends ZeusException {
	private static final long serialVersionUID = -4573955712491628431L;

	public ShiroConfigException(String message) {
        super(message);
    }

    public ShiroConfigException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public ShiroConfigException(ApiCode apiCode) {
        super(apiCode);
    }
}
