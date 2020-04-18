package cn.chinotan.zeus.redislock.lock;

/**
 * Created by ASUS on 2016/8/16.
 */
public class LockOperation {

    String name;
    String key;
    long timeout;//seconds
    long expire;//seconds
    String code;
    String msg;
    boolean autoUnlock;

    public LockOperation() {
    }

    public LockOperation(String name, String key, long timeout, long expire, String code, String msg, boolean autoUnlock) {
        this.name = name;
        this.key = key;
        this.timeout = timeout;
        this.expire = expire;
        this.code = code;
        this.msg = msg;
        this.autoUnlock = autoUnlock;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public boolean isAutoUnlock() {
        return autoUnlock;
    }

    public void setAutoUnlock(boolean autoUnlock) {
        this.autoUnlock = autoUnlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LockOperation that = (LockOperation) o;

        if (timeout != that.timeout) return false;
        if (expire != that.expire) return false;
        if (autoUnlock != that.autoUnlock) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return msg != null ? msg.equals(that.msg) : that.msg == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (int) (timeout ^ (timeout >>> 32));
        result = 31 * result + (int) (expire ^ (expire >>> 32));
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (autoUnlock ? 1 : 0);
        return result;
    }
}
