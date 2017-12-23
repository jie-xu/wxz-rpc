package com.github.wxz.rpc.parallel.semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SemaphoreWrapper
 * 信号量 封装
 *
 * @author xianzhi.wang
 * @date 2017/12/22 -12:51
 */
public class SemaphoreWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SemaphoreWrapper.class);
    protected final AtomicBoolean released = new AtomicBoolean(false);
    protected Semaphore semaphore;

    public SemaphoreWrapper() {
        semaphore = new Semaphore(1);
    }

    public SemaphoreWrapper(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public SemaphoreWrapper(int permits) {
        semaphore = new Semaphore(permits);
    }

    public SemaphoreWrapper(int permits, boolean fair) {
        semaphore = new Semaphore(permits, fair);
    }



    /**
     * 释放一个许可
     */
    public void release() {
        if (this.semaphore != null) {
            //cas
            if (this.released.compareAndSet(false, true)) {
                this.semaphore.release();
            }
        }
    }

    /**
     * acquire用来获取一个许可，若无许可能够获得，则会一直等待，直到获得许可。
     */
    public void acquire() {
        if (this.semaphore != null) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                LOGGER.error("acquire error...");
            }
        }
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public boolean isRelease() {
        return released.get();
    }
}

