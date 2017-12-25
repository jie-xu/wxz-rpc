
package com.github.wxz.core.rpc.parallel.semaphore;

/**
 * SemaphoreWrapperFactory
 * @author xianzhi.wang
 * @date 2017/12/22 -12:51
 */
public class SemaphoreWrapperFactory extends SemaphoreWrapper {
    private static final SemaphoreWrapperFactory INSTANCE = new SemaphoreWrapperFactory();

    public static SemaphoreWrapperFactory getInstance() {
        return INSTANCE;
    }

    private SemaphoreWrapperFactory() {
        super();
    }

    @Override
    public void acquire() {
        if (semaphore != null) {
            try {
                while (true) {
                    boolean result = released.get();
                    if (released.compareAndSet(result, true)) {
                        semaphore.acquire();
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void release() {
        if (semaphore != null) {
            while (true) {
                boolean result = released.get();
                if (released.compareAndSet(result, false)) {
                    semaphore.release();
                    break;
                }
            }
        }
    }
}

