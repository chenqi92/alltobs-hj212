package com.alltobs.hj212.lambda;

/**
 * 功能:
 *
 * @author chenQi
 */
@FunctionalInterface
public interface RunnableWithThrowable<T extends Throwable> {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     Thread#run()
     */
    public abstract void run() throws T;
}
