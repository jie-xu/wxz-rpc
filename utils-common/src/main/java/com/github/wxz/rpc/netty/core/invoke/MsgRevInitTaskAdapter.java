package com.github.wxz.rpc.netty.core.invoke;

import com.github.wxz.rpc.netty.core.invoke.AbstractMsgRevInitTask;
import com.github.wxz.rpc.netty.model.MsgRequest;
import com.github.wxz.rpc.netty.model.MsgResponse;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -13:16
 */
public class MsgRevInitTaskAdapter extends AbstractMsgRevInitTask {

    public MsgRevInitTaskAdapter(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        super(msgRequest, msgResponse, handlerMap);
    }
}
