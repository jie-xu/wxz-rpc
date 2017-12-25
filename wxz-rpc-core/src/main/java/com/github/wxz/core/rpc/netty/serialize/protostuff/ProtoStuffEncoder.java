package com.github.wxz.core.rpc.netty.serialize.protostuff;


import com.github.wxz.core.rpc.netty.serialize.MessageCodecUtil;
import com.github.wxz.core.rpc.netty.serialize.MessageEncoder;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffEncoder extends MessageEncoder {

    public ProtoStuffEncoder(MessageCodecUtil util) {
        super(util);
    }
}

