package cn.chinotan.zeus.redislock;

/**
 * Created by ASUS on 2016/8/16.
 */
public abstract class LockRunnable implements Runnable {

	AsyncLockExceptionHandler handler;
	String lockName;

	public LockRunnable(AsyncLockExceptionHandler handler, String lockName) {
		this.handler = handler;
		this.lockName = lockName;
	}

	public AsyncLockExceptionHandler getHandler() {
		return handler;
	}

	public String getLockName() {
		return lockName;
	}

}
