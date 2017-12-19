
package com.github.wxz.rpc.netty.seri;

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
     */
    void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline);
}

