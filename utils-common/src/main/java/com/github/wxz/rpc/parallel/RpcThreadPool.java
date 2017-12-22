package com.github.wxz.rpc.parallel;

import com.github.wxz.rpc.config.RpcSystemConfig;
import com.github.wxz.rpc.parallel.policy.*;
import com.github.wxz.rpc.parallel.queue.BlockingQueueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * rpc thread pool
 *
 * @author xianzhi.wang
 * @date 2017/12/20 -17:57
 */
public class RpcThreadPool {

    /**
     * 自定义拒绝策略
     *
     * @return
     */
    public static RejectedExecutionHandler createPolicy() {
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
    public static BlockingQueue<Runnable> createBlockingQueue(int queues) {
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
