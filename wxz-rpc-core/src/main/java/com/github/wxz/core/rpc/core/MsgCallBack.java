package com.github.wxz.core.rpc.core;


import com.github.wxz.core.config.RpcSystemConfig;
import com.github.wxz.core.exception.InvokeModuleException;
import com.github.wxz.core.exception.InvokeTimeoutException;
import com.github.wxz.core.exception.RejectResponeException;
import com.github.wxz.core.rpc.model.MsgRequest;
import com.github.wxz.core.rpc.model.MsgResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -18:20
 */
public class MsgCallBack {
    private MsgRequest msgRequest;
    private MsgResponse msgResponse;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();

    public MsgCallBack(MsgRequest msgRequest) {
        this.msgRequest = msgRequest;
    }

    public Object execute() {
        try {
            lock.lock();
            //等待超时机制
            await();

            if (this.msgResponse != null) {
                boolean success = getInvokeResult();
                if (success) {
                    if (this.msgResponse.getError().isEmpty()) {
                        return this.msgResponse.getResult();
                    } else {
                        throw new InvokeModuleException(this.msgResponse.getError());
                    }
                } else {
                    throw new RejectResponeException(RpcSystemConfig.FILTER_RESPONSE_MSG);
                }
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * finish.signal
     *
     * @param msgResponse
     */
    public void over(MsgResponse msgResponse) {
        try {
            lock.lock();
            finish.signal();
            this.msgResponse = msgResponse;
        } finally {
            lock.unlock();
        }
    }

    /**
     * finish.await;
     */
    private void await() {
        boolean timeout = false;
        try {
            timeout = finish.await(RpcSystemConfig.SYSTEM_PROPERTY_MESSAGE_CALLBACK_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!timeout) {
            throw new InvokeTimeoutException(RpcSystemConfig.TIMEOUT_RESPONSE_MSG);
        }
    }

    private boolean getInvokeResult() {
        return (!this.msgResponse.getError().equals(RpcSystemConfig.FILTER_RESPONSE_MSG)
                &&
                (!this.msgResponse.isNotNull()
                        || (this.msgResponse.isNotNull() && this.msgResponse.getResult() != null)));
    }
}
