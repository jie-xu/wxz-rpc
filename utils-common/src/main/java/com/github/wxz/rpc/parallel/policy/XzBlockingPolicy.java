package com.github.wxz.rpc.parallel.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -18:15
 */
public class XzBlockingPolicy implements RejectedExecutionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(XzBlockingPolicy.class);

    private String threadName;

    public XzBlockingPolicy() {
        this(null);
    }

    public XzBlockingPolicy(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOGGER.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }

        if (!executor.isShutdown()) {
            try {
                executor.getQueue().put(runnable);
            } catch (InterruptedException e) {
                LOGGER.error("put runnable error....");
            }
        }
    }
}
