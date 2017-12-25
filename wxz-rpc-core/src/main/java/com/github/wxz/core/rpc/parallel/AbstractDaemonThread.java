package com.github.wxz.core.rpc.parallel;

/**
 * @author xianzhi.wang
 * @date 2017/12/22 -14:52
 */
public abstract class AbstractDaemonThread implements Runnable {
    private static final long JOIN_TIME = 90 * 1000L;
    protected final Thread thread;
    protected volatile boolean hasNotified = false;
    protected volatile boolean stoped = false;

    public AbstractDaemonThread() {
        this.thread = new Thread(this, this.getDaemonThreadName());
    }

    /**
     * getDaemonThreadName
     *
     * @return
     */
    public abstract String getDaemonThreadName();

    public void start() {
        this.thread.start();
    }

    public void shutdown() {
        this.shutdown(false);
    }

    public void stop() {
        this.stop(false);
    }

    public void makeStop() {
        this.stoped = true;
    }


    public void stop(final boolean interrupt) {
        this.stoped = true;
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }

        if (interrupt) {
            this.thread.interrupt();
        }
    }


    public void shutdown(final boolean interrupt) {
        this.stoped = true;
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }

        try {
            if (interrupt) {
                this.thread.interrupt();
            }

            if (!this.thread.isDaemon()) {
                this.thread.join(this.getJoinTime());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void wakeup() {
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }
    }

    protected void waitForRunning(long interval) {
        synchronized (this) {
            if (this.hasNotified) {
                this.hasNotified = false;
                this.onWaitEnd();
                return;
            }

            try {
                this.wait(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.hasNotified = false;
                this.onWaitEnd();
            }
        }
    }

    protected void onWaitEnd() {
    }

    public boolean isStoped() {
        return stoped;
    }

    public long getJoinTime() {
        return JOIN_TIME;
    }
}

