package com.github.wxz.rpc.netty.handler;

import io.netty.channel.ChannelPipeline;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public interface RpcSendHandler {
    /**
     * handle
     *
     * @param pipeline
     */
    void handle(ChannelPipeline pipeline);
}

