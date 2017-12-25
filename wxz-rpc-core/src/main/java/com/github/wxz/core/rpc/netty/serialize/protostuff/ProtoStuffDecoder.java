package com.github.wxz.core.rpc.netty.serialize.protostuff;


import com.github.wxz.core.rpc.netty.serialize.MessageCodecUtil;
import com.github.wxz.core.rpc.netty.serialize.MessageDecoder;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffDecoder extends MessageDecoder {

    public ProtoStuffDecoder(MessageCodecUtil util) {
        super(util);
    }
}

