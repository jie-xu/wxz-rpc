package com.github.wxz.rpc.parallel.policy;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -18:08
 */
public interface XzRejectedRunnable extends Runnable {
    /**
     * rejected
     */
    void rejected();
}
