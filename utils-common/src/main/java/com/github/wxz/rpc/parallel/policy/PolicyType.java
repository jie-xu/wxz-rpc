package com.github.wxz.rpc.parallel.policy;

/**
 * 线程池拒绝策略
 *
 * @author xianzhi.wang
 * @date 2017/12/20 -18:02
 */
public enum PolicyType {
    /**
     * java线程池默认的阻塞策略，不执行此任务
     */
    ABORT_POLICY("XzAbortPolicy"),
    /**
     *
     */
    BLOCKING_POLICY("XzBlockingPolicy"),
    /**
     * 在调用execute的线程里面执行此command，会阻塞入口
     */
    CALLER_RUNS_POLICY("XzCallerRunsPolicy"),
    /**
     *
     */
    DISCARDED_POLICY("XzDiscardedPolicy"),
    /**
     *
     */
    REJECTED_POLICY("XzRejectedPolicy");

    private String value;

    private PolicyType(String value) {
        this.value = value;
    }

    public static PolicyType fromString(String value) {
        for (PolicyType type : PolicyType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
