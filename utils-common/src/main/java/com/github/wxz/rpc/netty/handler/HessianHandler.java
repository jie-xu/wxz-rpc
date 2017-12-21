
package com.github.wxz.rpc.netty.handler;

import com.github.wxz.rpc.netty.core.recv.MsgRevHandler;
import com.github.wxz.rpc.netty.core.send.MsgSendHandler;
import com.github.wxz.rpc.netty.serialize.hessian.HessianCodecUtil;
import com.github.wxz.rpc.netty.serialize.hessian.HessianDecoder;
import com.github.wxz.rpc.netty.serialize.hessian.HessianEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class HessianHandler implements RpcHandler {

    @Override
    public void sendHandle(ChannelPipeline pipeline) {
        HessianCodecUtil util = new HessianCodecUtil();
        pipeline.addLast(new HessianEncoder(util));
        pipeline.addLast(new HessianDecoder(util));
        pipeline.addLast("logging", new LoggingHandler(LogLevel.WARN));
        pipeline.addLast(new MsgSendHandler());
    }

    @Override
    public void recHandle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        HessianCodecUtil util = new HessianCodecUtil();
        pipeline.addLast(new HessianEncoder(util));
        pipeline.addLast(new HessianDecoder(util));
        pipeline.addLast("logging",new LoggingHandler(LogLevel.WARN));
        pipeline.addLast(new MsgRevHandler(handlerMap));
    }
}

