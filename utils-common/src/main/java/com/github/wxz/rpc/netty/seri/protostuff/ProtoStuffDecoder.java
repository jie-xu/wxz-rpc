package com.github.wxz.rpc.netty.seri.protostuff;

import com.github.wxz.rpc.netty.seri.MessageCodecUtil;
import com.github.wxz.rpc.netty.seri.MessageDecoder;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffDecoder extends MessageDecoder {

    public ProtoStuffDecoder(MessageCodecUtil util) {
        super(util);
    }
}

