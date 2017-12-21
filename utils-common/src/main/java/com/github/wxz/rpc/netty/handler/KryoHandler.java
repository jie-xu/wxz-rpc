package com.github.wxz.rpc.netty.handler;

import com.github.wxz.rpc.netty.core.recv.MsgRevHandler;
import com.github.wxz.rpc.netty.core.send.MsgSendHandler;
import com.github.wxz.rpc.netty.serialize.kryo.KryoCodecUtil;
import com.github.wxz.rpc.netty.serialize.kryo.KryoDecoder;
import com.github.wxz.rpc.netty.serialize.kryo.KryoEncoder;
import com.github.wxz.rpc.netty.serialize.kryo.KryoPoolFactory;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class KryoHandler implements RpcHandler {
    @Override
    public void sendHandle(ChannelPipeline pipeline) {
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        pipeline.addLast(new KryoEncoder(util));
        pipeline.addLast(new KryoDecoder(util));
        pipeline.addLast("logging", new LoggingHandler(LogLevel.WARN));
        pipeline.addLast(new MsgSendHandler());
    }

    @Override
    public void recHandle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        pipeline.addLast(new KryoEncoder(util));
        pipeline.addLast(new KryoDecoder(util));
        pipeline.addLast("logging", new LoggingHandler(LogLevel.WARN));
        pipeline.addLast(new MsgRevHandler(handlerMap));
    }
}

