package com.github.wxz.rpc.netty.handler;

import com.github.wxz.rpc.netty.core.MsgRecvHandler;
import com.github.wxz.rpc.netty.seri.protostuff.ProtoStuffCodecUtil;
import com.github.wxz.rpc.netty.seri.protostuff.ProtoStuffDecoder;
import com.github.wxz.rpc.netty.seri.protostuff.ProtoStuffEncoder;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffRecvHandler implements RpcRecvHandler {
    @Override
    public void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        ProtoStuffCodecUtil util = new ProtoStuffCodecUtil();
        util.setRpcDirect(true);
        pipeline.addLast(new ProtoStuffEncoder(util));
        pipeline.addLast(new ProtoStuffDecoder(util));
        pipeline.addLast(new MsgRecvHandler(handlerMap));
    }
}

