
package com.github.wxz.rpc.netty.serialize;

import com.github.wxz.rpc.netty.handler.HandlerType;
import io.netty.channel.ChannelPipeline;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public interface RpcSerializeFrame {
    /**
     * select
     * @param protocol
     * @param pipeline
     * @param type type
     */
    void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline,HandlerType type);
}

