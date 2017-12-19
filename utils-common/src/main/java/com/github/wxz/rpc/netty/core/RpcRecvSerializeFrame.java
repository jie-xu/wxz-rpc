package com.github.wxz.rpc.netty.core;

import com.github.wxz.rpc.netty.handler.*;
import com.github.wxz.rpc.netty.seri.RpcSerializeFrame;
import com.github.wxz.rpc.netty.seri.RpcSerializeProtocol;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -17:47
 */
public class RpcRecvSerializeFrame implements RpcSerializeFrame {

    private static ClassToInstanceMap<RpcRecvHandler> handler = MutableClassToInstanceMap.create();

    static {
        handler.putInstance(JdkNativeRecvHandler.class, new JdkNativeRecvHandler());
        handler.putInstance(KryoRecvHandler.class, new KryoRecvHandler());
        handler.putInstance(HessianRecvHandler.class, new HessianRecvHandler());
        handler.putInstance(ProtoStuffRecvHandler.class, new ProtoStuffRecvHandler());
    }

    private Map<String, Object> handlerMap = null;

    public RpcRecvSerializeFrame(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case JDK_SERIALIZE: {
                handler.getInstance(JdkNativeRecvHandler.class).handle(handlerMap, pipeline);
                break;
            }
            case KRYO_SERIALIZE: {
                handler.getInstance(KryoRecvHandler.class).handle(handlerMap, pipeline);
                break;
            }
            case HESSIAN_SERIALIZE: {
                handler.getInstance(HessianRecvHandler.class).handle(handlerMap, pipeline);
                break;
            }
            case PROTO_STUFF_SERIALIZE: {
                handler.getInstance(ProtoStuffRecvHandler.class).handle(handlerMap, pipeline);
                break;
            }
            default: {
                break;
            }
        }
    }
}
