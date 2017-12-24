package com.github.wxz.rpc.parallel;

import com.github.wxz.config.RpcSystemConfig;
import com.github.wxz.rpc.parallel.policy.*;
import com.github.wxz.rpc.parallel.queue.BlockingQueueType;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

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
            createBlockingQueue(
                    RpcSystemConfig.SYSTEM_PROPERTY_THREAD_POOL_QUEUE_NUMS),
            new NamedThreadFactory("http-thread-pool@", true),
            createPolicy());

    private static ListeningExecutorService ListeningThreadPoolExecutor =
            MoreExecutors.listeningDecorator(
                    threadPoolExecutor);

    /**
     * 快速 execute 1
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }


    /**
     * 快速 execute 2
     *
     * @param runnable
     */
    public static void execute(ThreadPoolExecutor executor,Runnable runnable) {
        executor.execute(runnable);
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
     * jmx ThreadPoolExecutor
     * @param threads
     * @return
     */
    public static ThreadPoolExecutor getJMXThreadPoolExecutor(int threads){
        return new ThreadPoolExecutor(
                threads,
                threads,
                0L, TimeUnit.MILLISECONDS,
                createBlockingQueue(
                        -1),
                new NamedThreadFactory("jmx", true),
                createPolicy());
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
                createBlockingQueue(
                        queues),
                new NamedThreadFactory(name, true),
                createPolicy());
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

    /**
     * 自定义拒绝策略
     *
     * @return
     */
    private static RejectedExecutionHandler createPolicy() {
        PolicyType policyType = PolicyType.fromString(System.getProperty(RpcSystemConfig.SYSTEM_PROPERTY_THREAD_POOL_REJECTED_POLICY_ATTR, "XzAbortPolicy"));
        switch (policyType) {
            case BLOCKING_POLICY:
                return new XzBlockingPolicy();
            case CALLER_RUNS_POLICY:
                return new XzCallerRunsPolicy();
            case ABORT_POLICY:
                return new XzAbortPolicy();
            case REJECTED_POLICY:
                return new XzRejectedPolicy();
            case DISCARDED_POLICY:
                return new XzDiscardedPolicy();
            default: {
                break;
            }
        }
        return null;
    }

    /**
     * createBlockingQueue
     *
     * @param queues
     * @return
     */
    private static BlockingQueue<Runnable> createBlockingQueue(int queues) {
        BlockingQueueType queueType = BlockingQueueType.fromString(System.getProperty(RpcSystemConfig.SYSTEM_PROPERTY_THREAD_POOL_QUEUE_NAME_ATTR, "LinkedBlockingQueue"));
        switch (queueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<>();
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<>(RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL * queues);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<>();
            default: {
                break;
            }
        }
        return null;
    }
}
