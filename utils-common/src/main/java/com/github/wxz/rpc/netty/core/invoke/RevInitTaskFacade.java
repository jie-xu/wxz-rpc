package com.github.wxz.rpc.netty.core.invoke;

import com.github.wxz.rpc.netty.core.invoke.MsgRevInitTaskAdapter;
import com.github.wxz.rpc.netty.model.MsgRequest;
import com.github.wxz.rpc.netty.model.MsgResponse;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -13:14
 */
public class RevInitTaskFacade {
    private MsgRequest msgRequest;
    private MsgResponse msgResponse;
    private Map<String, Object> handlerMap;

    public RevInitTaskFacade(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        this.msgRequest = msgRequest;
        this.msgResponse = msgResponse;
        this.handlerMap = handlerMap;
    }

    public Callable<Boolean> getTask() {
        return new MsgRevInitTaskAdapter(msgRequest, msgResponse, handlerMap);
    }

}
