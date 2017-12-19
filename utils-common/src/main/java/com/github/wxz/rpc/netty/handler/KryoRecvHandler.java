
package com.github.wxz.rpc.netty.handler;

import com.github.wxz.rpc.netty.core.MsgRecvHandler;
import com.github.wxz.rpc.netty.seri.kryo.KryoCodecUtil;
import com.github.wxz.rpc.netty.seri.kryo.KryoDecoder;
import com.github.wxz.rpc.netty.seri.kryo.KryoEncoder;
import com.github.wxz.rpc.netty.seri.kryo.KryoPoolFactory;
import io.netty.channel.ChannelPipeline;

import java.util.Map;


public class KryoRecvHandler implements RpcRecvHandler {
    @Override
    public void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        pipeline.addLast(new KryoEncoder(util));
        pipeline.addLast(new KryoDecoder(util));
        pipeline.addLast(new MsgRecvHandler(handlerMap));
    }
}

