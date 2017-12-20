
package com.github.wxz.rpc.netty.parallel.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -18:08
 */
public class XzCallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy {
    private static final Logger LOGGER = LoggerFactory.getLogger(XzCallerRunsPolicy.class);

    private String threadName;

    public XzCallerRunsPolicy() {
        this(null);
    }

    public XzCallerRunsPolicy(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOGGER.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }

        super.rejectedExecution(runnable, executor);
    }
}

