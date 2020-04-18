package cn.chinotan.zeus.redislock;

/**
 * Created by ASUS on 2016/8/16.
 */
public class AsyncLockExceptionHandler implements Thread.UncaughtExceptionHandler {

    Throwable error;

    public Throwable getError() {
        return error;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.error = (Exception) e;
    }

}
