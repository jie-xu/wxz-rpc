package com.github.wxz.rpc.netty.parallel.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -18:08
 */
public class XzAbortPolicy extends AbortPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(XzAbortPolicy.class);

    private String threadName;

    public XzAbortPolicy() {
        this(null);
    }

    public XzAbortPolicy(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOGGER.error("rpc Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }
        LOGGER.info("rpc server[ Thread Name: {}, Pool Size: {} (active: {}, core: {}, max: {}, "
                        + "largest: {}), Task: {} (completed: {}),"
                        + " Executor status:(isShutdown:{}, isTerminated:{}, isTerminating:{})]",
                threadName, executor.getPoolSize(),
                executor.getActiveCount(), executor.getCorePoolSize(),
                executor.getMaximumPoolSize(), executor.getLargestPoolSize(),
                executor.getTaskCount(), executor.getCompletedTaskCount(),
                executor.isShutdown(), executor.isTerminated(),
                executor.isTerminating());

        super.rejectedExecution(runnable, executor);
    }

}
