package com.github.wxz.rpc.netty.handler;

import com.github.wxz.rpc.netty.core.MsgSendHandler;
import com.github.wxz.rpc.netty.seri.kryo.KryoCodecUtil;
import com.github.wxz.rpc.netty.seri.kryo.KryoDecoder;
import com.github.wxz.rpc.netty.seri.kryo.KryoEncoder;
import com.github.wxz.rpc.netty.seri.kryo.KryoPoolFactory;
import io.netty.channel.ChannelPipeline;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class KryoSendHandler implements RpcSendHandler {
    @Override
    public void handle(ChannelPipeline pipeline) {
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        pipeline.addLast(new KryoEncoder(util));
        pipeline.addLast(new KryoDecoder(util));
        pipeline.addLast(new MsgSendHandler());
    }
}

