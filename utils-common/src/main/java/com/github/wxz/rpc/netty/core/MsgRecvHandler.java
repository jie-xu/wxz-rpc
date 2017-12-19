
package com.github.wxz.rpc.netty.core;

import com.github.wxz.rpc.model.MessageRequest;
import com.github.wxz.rpc.model.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -17:11
 */
public class MsgRecvHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> handlerMap;

    public MsgRecvHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRequest request = (MessageRequest) msg;
        MessageResponse response = new MessageResponse();
       // RecvInitializeTaskFacade facade = new RecvInitializeTaskFacade(request, response, handlerMap);
        //Callable<Boolean> recvTask = facade.getTask();
        //MsgRecvExecutor.submit(recvTask, ctx, request, response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

