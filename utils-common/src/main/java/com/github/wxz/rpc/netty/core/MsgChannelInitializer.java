package com.github.wxz.rpc.netty.core;

import com.github.wxz.rpc.netty.handler.HandlerType;
import com.github.wxz.rpc.netty.serialize.RpcSerializeProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -17:47
 */
public class MsgChannelInitializer extends ChannelInitializer<SocketChannel> {

    private RpcSerializeProtocol protocol;
    private HandlerType handlerType;
    private RpcSerializeFrameImpl frame = null;

    public MsgChannelInitializer(Map<String, Object> handlerMap) {
        frame = new RpcSerializeFrameImpl(handlerMap);
    }

    public MsgChannelInitializer() {
        frame = new RpcSerializeFrameImpl();
    }

    public MsgChannelInitializer buildRpcSerializeProtocol(RpcSerializeProtocol protocol, HandlerType handlerType) {
        this.protocol = protocol;
        this.handlerType = handlerType;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        frame.select(protocol, pipeline, handlerType);
    }


}
