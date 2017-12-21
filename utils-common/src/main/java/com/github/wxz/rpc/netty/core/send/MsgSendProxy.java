package com.github.wxz.rpc.netty.core.send;

import com.github.wxz.rpc.netty.core.MsgCallBack;
import com.github.wxz.rpc.netty.model.MsgRequest;
import com.google.common.reflect.AbstractInvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -9:57
 */
public class MsgSendProxy<T> extends AbstractInvocationHandler {

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] objects) throws Throwable {

        MsgRequest msgRequest = new MsgRequest();
        msgRequest.setMessageId(UUID.randomUUID().toString());
        msgRequest.setClassName(method.getDeclaringClass().getName());
        msgRequest.setMethodName(method.getName());
        msgRequest.setTypeParameters(method.getParameterTypes());
        msgRequest.setParametersVal(objects);

        MsgSendHandler msgSendHandler = RpcServerLoader.getInstance().getMsgSendHandler();
        MsgCallBack msgCallBack = msgSendHandler.sendRequest(msgRequest);
        return msgCallBack.execute();

    }

}
