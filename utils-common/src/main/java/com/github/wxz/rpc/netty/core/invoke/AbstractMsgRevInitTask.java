package com.github.wxz.rpc.netty.core.invoke;

import com.github.wxz.rpc.netty.model.MsgRequest;
import com.github.wxz.rpc.netty.model.MsgResponse;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -13:22
 */
public abstract class AbstractMsgRevInitTask implements Callable<Boolean> {
    protected MsgRequest msgRequest = null;
    protected MsgResponse msgResponse = null;
    protected Map<String, Object> handlerMap = null;


    public AbstractMsgRevInitTask(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        this.msgRequest = msgRequest;
        this.msgResponse = msgResponse;
        this.handlerMap = handlerMap;
    }

    @Override
    public Boolean call() {
        return true;
    }

}
