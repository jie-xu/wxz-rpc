package com.github.wxz.rpc.netty.core;

import com.github.wxz.RpcSystemConfig;
import com.github.wxz.rpc.netty.parallel.NamedThreadFactory;
import com.github.wxz.rpc.netty.parallel.RpcThreadPool;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -14:52
 */
public class ExecutorManager {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2,
            0L, TimeUnit.MILLISECONDS,
            RpcThreadPool.createBlockingQueue(
                    RpcSystemConfig.SYSTEM_PROPERTY_THREAD_POOL_QUEUE_NUMS),
            new NamedThreadFactory("rpcThreadPool", true),
            RpcThreadPool.createPolicy());

    public static void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

}
