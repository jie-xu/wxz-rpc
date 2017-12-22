package com.github.wxz.rpc.parallel;

import com.github.wxz.rpc.config.RpcSystemConfig;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程封装类
 *
 * @author xianzhi.wang
 * @date 2017/12/20 -14:52
 */
public class ExecutorManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorManager.class);

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2,
            0L, TimeUnit.MILLISECONDS,
            RpcThreadPool.createBlockingQueue(
                    RpcSystemConfig.SYSTEM_PROPERTY_THREAD_POOL_QUEUE_NUMS),
            new NamedThreadFactory("rpcThreadPool", true),
            RpcThreadPool.createPolicy());

    private static ListeningExecutorService ListeningThreadPoolExecutor =
            MoreExecutors.listeningDecorator(
                    threadPoolExecutor);

    /**
     * 快速 execute
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    /**
     * 快速 submit
     *
     * @param callable
     * @param <T>
     * @return
     */
    public static <T> ListenableFuture<T> submit(Callable<T> callable) {
        return ListeningThreadPoolExecutor.submit(callable);
    }


    /**
     * 获取线程池
     *
     * @param name
     * @param threads
     * @param queues
     * @return
     */
    public static ThreadPoolExecutor getThreadPoolExecutor(String name, int threads, int queues) {
        LOGGER.info("ThreadPool Core name : " + name + " [threads:" + threads + ", queues:" + queues + "]");
        return new ThreadPoolExecutor(
                threads,
                threads,
                0L, TimeUnit.MILLISECONDS,
                RpcThreadPool.createBlockingQueue(
                        queues),
                new NamedThreadFactory(name, true),
                RpcThreadPool.createPolicy());
    }


    /**
     * guava submit
     *
     * @param callable
     * @param <T>
     * @return
     */
    public static <T> ListenableFuture<T> submit(ThreadPoolExecutor executor, Callable<T> callable) {
        return getListeningExecutorService(executor).submit(callable);
    }


    /**
     * @param executor
     * @return
     */
    private static ListeningExecutorService getListeningExecutorService(ThreadPoolExecutor executor) {
        return MoreExecutors.listeningDecorator(
                executor);
    }

}
