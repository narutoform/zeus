package cn.chinotan.zeus.redislock.lock;

/**
 * Created by ASUS on 2016/8/16.
 */
public interface LockOperationInvoker {

    /**
     * Invoke the cache operation defined by this instance. Wraps any
     * exception that is thrown during the invocation in a
     * {@link LockOperationInvoker.ThrowableWrapper}
     * .
     *
     * @return the result of the operation
     * @throws LockOperationInvoker.ThrowableWrapper if an error occurred while invoking the operation
     */
    Object invoke() throws ThrowableWrapper;

    /**
     * Wrap any exception thrown while invoking {@link #invoke()}
     */
    @SuppressWarnings("serial")
    class ThrowableWrapper extends RuntimeException {

        private final Throwable original;

        public ThrowableWrapper(Throwable original) {
            super(original.getMessage(), original);
            this.original = original;
        }

        public Throwable getOriginal() {
            return original;
        }
    }
}
