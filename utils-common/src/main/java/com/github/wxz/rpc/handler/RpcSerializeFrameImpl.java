package com.github.wxz.rpc.handler;

import com.github.wxz.rpc.netty.handler.*;
import com.github.wxz.rpc.netty.serialize.RpcSerializeFrame;
import com.github.wxz.rpc.netty.serialize.RpcSerializeProtocol;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

/**
 * RpcSerializeFrameImpl
 * @author xianzhi.wang
 * @date 2017/12/19 -17:47
 */
public class RpcSerializeFrameImpl implements RpcSerializeFrame {

    private static ClassToInstanceMap<RpcHandler> handler = MutableClassToInstanceMap.create();

    static {
        handler.putInstance(JdkNativeHandler.class, new JdkNativeHandler());
        handler.putInstance(KryoHandler.class, new KryoHandler());
        handler.putInstance(HessianHandler.class, new HessianHandler());
        handler.putInstance(ProtoStuffHandler.class, new ProtoStuffHandler());
    }

    private Map<String, Object> handlerMap = null;

    public RpcSerializeFrameImpl() {

    }

    public RpcSerializeFrameImpl(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline, HandlerType handlerType) {
        switch (protocol) {
            case JDK_SERIALIZE: {
                if (handlerType == HandlerType.REC) {
                    handler.getInstance(JdkNativeHandler.class).recHandle(handlerMap, pipeline);
                } else if (handlerType == HandlerType.SEND) {
                    handler.getInstance(JdkNativeHandler.class).sendHandle(pipeline);
                }
                break;
            }
            case KR_YO_SERIALIZE: {
                if (handlerType == HandlerType.REC) {
                    handler.getInstance(KryoHandler.class).recHandle(handlerMap, pipeline);
                } else if (handlerType == HandlerType.SEND) {
                    handler.getInstance(KryoHandler.class).sendHandle(pipeline);
                }
                break;
            }
            case HESSIAN_SERIALIZE: {
                if (handlerType == HandlerType.REC) {
                    handler.getInstance(HessianHandler.class).recHandle(handlerMap, pipeline);
                } else if (handlerType == HandlerType.SEND) {
                    handler.getInstance(HessianHandler.class).sendHandle(pipeline);
                }
                break;
            }
            case PROTO_STUFF_SERIALIZE: {
                if (handlerType == HandlerType.REC) {
                    handler.getInstance(ProtoStuffHandler.class).recHandle(handlerMap, pipeline);
                } else if (handlerType == HandlerType.SEND) {
                    handler.getInstance(ProtoStuffHandler.class).sendHandle(pipeline);
                }
                break;
            }
            default: {
                break;
            }
        }
    }
}
