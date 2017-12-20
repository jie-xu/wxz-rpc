
package com.github.wxz.rpc.netty.handler;

import com.github.wxz.rpc.netty.core.MsgSendHandler;
import com.github.wxz.rpc.netty.seri.protostuff.ProtoStuffCodecUtil;
import com.github.wxz.rpc.netty.seri.protostuff.ProtoStuffDecoder;
import com.github.wxz.rpc.netty.seri.protostuff.ProtoStuffEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffSendHandler implements RpcSendHandler {
    @Override
    public void handle(ChannelPipeline pipeline) {
        ProtoStuffCodecUtil util = new ProtoStuffCodecUtil();
        util.setRpcDirect(false);
        pipeline.addLast(new ProtoStuffEncoder(util));
        pipeline.addLast(new ProtoStuffDecoder(util));
        pipeline.addLast("logging", new LoggingHandler(LogLevel.WARN));
        pipeline.addLast(new MsgSendHandler());
    }
}
