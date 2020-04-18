package cn.chinotan.zeus.redislock.exception;

/**
 * Created by ASUS on 2016/8/16.
 */
public class LockException extends RuntimeException {

	private String code;

	public LockException() {
		super();
	}

	public LockException(String message) {
		super(message);
	}

	public LockException(String code, String message) {
		super(message);
		this.code = code;
	}

	public LockException(String message, Throwable cause) {
		super(message, cause);
	}

	public LockException(Throwable cause) {
		super(cause);
	}

	public String getCode() {
		return code;
	}
}
