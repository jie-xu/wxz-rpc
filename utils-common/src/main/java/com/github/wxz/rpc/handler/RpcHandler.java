package com.github.wxz.rpc.handler;

import io.netty.channel.ChannelPipeline;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public interface RpcHandler {
    /**
     * send recHandle
     *
     * @param pipeline
     */
    void sendHandle(ChannelPipeline pipeline);

    /**
     * rec recHandle
     *
     * @param handlerMap
     * @param pipeline
     */
    void recHandle(Map<String, Object> handlerMap, ChannelPipeline pipeline);
}

