
package com.github.wxz.rpc.netty.handler;

import io.netty.channel.ChannelPipeline;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public interface RpcRecvHandler {
    /**
     * handle
     * @param handlerMap
     * @param pipeline
     */
    void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline);
}

