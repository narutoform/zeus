package cn.chinotan.zeus.framework.common.exception;

import cn.chinotan.zeus.framework.common.api.ApiCode;

/**
 * DAO异常
 *
 * @author xingcheng
 * @date 2018-11-08
 */
public class DaoException extends ZeusException {
	private static final long serialVersionUID = -6912618737345878854L;

	public DaoException(String message) {
        super(message);
    }

    public DaoException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public DaoException(ApiCode apiCode) {
        super(apiCode);
    }
}
