package com.github.wxz.rpc.handler;

import com.github.wxz.rpc.core.recv.MsgRevHandler;
import com.github.wxz.rpc.core.send.MsgSendHandler;
import com.github.wxz.rpc.netty.serialize.protostuff.ProtoStuffCodecUtil;
import com.github.wxz.rpc.netty.serialize.protostuff.ProtoStuffDecoder;
import com.github.wxz.rpc.netty.serialize.protostuff.ProtoStuffEncoder;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffHandler implements RpcHandler {

    @Override
    public void sendHandle(ChannelPipeline pipeline) {
        ProtoStuffCodecUtil util = new ProtoStuffCodecUtil();
        util.setRpcDirect(false);
//        pipeline.addLast(new ProtobufDecoder())
        pipeline.addLast(new ProtoStuffEncoder(util));
        pipeline.addLast(new ProtoStuffDecoder(util));
        //pipeline.addLast("logging", new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new MsgSendHandler());
    }

    @Override
    public void recHandle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        ProtoStuffCodecUtil util = new ProtoStuffCodecUtil();
        util.setRpcDirect(true);
//        pipeline.addLast(n)
        pipeline.addLast(new ProtoStuffEncoder(util));
        pipeline.addLast(new ProtoStuffDecoder(util));
        //pipeline.addLast("logging", new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new MsgRevHandler(handlerMap));
    }
}

