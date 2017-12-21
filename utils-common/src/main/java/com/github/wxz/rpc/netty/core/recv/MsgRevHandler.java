package com.github.wxz.rpc.netty.core.recv;

import com.alibaba.fastjson.JSON;
import com.github.wxz.rpc.netty.core.invoke.RevInitTaskFacade;
import com.github.wxz.rpc.netty.model.MsgRequest;
import com.github.wxz.rpc.netty.model.MsgResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -17:11
 */
public class MsgRevHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgRevHandler.class);
    private final Map<String, Object> handlerMap;

    public MsgRevHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MsgRequest request = (MsgRequest) msg;
        MsgResponse response = new MsgResponse();
        LOGGER.info("request", JSON.toJSONString(msg));

        RevInitTaskFacade facade = new RevInitTaskFacade(request, response, handlerMap);
        Callable<Boolean> recvTask = facade.getTask();
        //MsgRevExecutor.submit(recvTask, ctx, request, response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

