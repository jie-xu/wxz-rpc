
package com.github.wxz.rpc.parallel.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -18:08
 */
public class XzDiscardedPolicy implements RejectedExecutionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(XzDiscardedPolicy.class);

    private String threadName;

    public XzDiscardedPolicy() {
        this(null);
    }

    public XzDiscardedPolicy(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOGGER.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }

        if (!executor.isShutdown()) {
            BlockingQueue<Runnable> queue = executor.getQueue();
            int discardSize = queue.size() >> 1;
            for (int i = 0; i < discardSize; i++) {
                queue.poll();
            }

            queue.offer(runnable);
        }
    }
}
