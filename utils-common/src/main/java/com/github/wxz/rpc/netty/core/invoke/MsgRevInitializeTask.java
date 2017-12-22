package com.github.wxz.rpc.netty.core.invoke;

import com.github.wxz.rpc.netty.model.MsgRequest;
import com.github.wxz.rpc.netty.model.MsgResponse;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/22 -17:53
 */
public class MsgRevInitializeTask extends AbstractMsgRevInitTask {

    public MsgRevInitializeTask(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        super(msgRequest, msgResponse, handlerMap);
    }

    @Override
    protected void injectInvoke() {

    }

    @Override
    protected void injectSuccessInvoke(long invokeTimeStamp) {

    }

    @Override
    protected void injectFailInvoke(Throwable error) {

    }

    @Override
    protected void injectFilterInvoke() {

    }

    @Override
    protected void acquire() {

    }

    @Override
    protected void release() {

    }
}
