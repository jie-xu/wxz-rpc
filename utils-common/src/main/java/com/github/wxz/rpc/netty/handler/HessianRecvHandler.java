
package com.github.wxz.rpc.netty.handler;

import com.github.wxz.rpc.netty.core.MsgRecvHandler;
import com.github.wxz.rpc.netty.seri.hessian.HessianCodecUtil;
import com.github.wxz.rpc.netty.seri.hessian.HessianDecoder;
import com.github.wxz.rpc.netty.seri.hessian.HessianEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class HessianRecvHandler implements RpcRecvHandler {
    @Override
    public void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        HessianCodecUtil util = new HessianCodecUtil();
        pipeline.addLast(new HessianEncoder(util));
        pipeline.addLast(new HessianDecoder(util));
        pipeline.addLast("logging",new LoggingHandler(LogLevel.WARN));
        pipeline.addLast(new MsgRecvHandler(handlerMap));
    }
}

