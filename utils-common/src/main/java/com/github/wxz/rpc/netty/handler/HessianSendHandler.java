
package com.github.wxz.rpc.netty.handler;

import com.github.wxz.rpc.netty.core.MsgSendHandler;
import com.github.wxz.rpc.netty.seri.hessian.HessianCodecUtil;
import com.github.wxz.rpc.netty.seri.hessian.HessianDecoder;
import com.github.wxz.rpc.netty.seri.hessian.HessianEncoder;
import io.netty.channel.ChannelPipeline;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class HessianSendHandler implements RpcSendHandler {
    @Override
    public void handle(ChannelPipeline pipeline) {
        HessianCodecUtil util = new HessianCodecUtil();
        pipeline.addLast(new HessianEncoder(util));
        pipeline.addLast(new HessianDecoder(util));
        pipeline.addLast(new MsgSendHandler());
    }
}

