package com.github.wxz.core.rpc.netty.serialize.kryo;

import com.github.wxz.core.rpc.netty.serialize.MessageCodecUtil;
import com.github.wxz.core.rpc.netty.serialize.MessageDecoder;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class KryoDecoder extends MessageDecoder {

    public KryoDecoder(MessageCodecUtil util) {
        super(util);
    }
}
