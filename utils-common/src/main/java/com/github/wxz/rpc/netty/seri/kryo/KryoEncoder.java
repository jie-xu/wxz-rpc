package com.github.wxz.rpc.netty.seri.kryo;


import com.github.wxz.rpc.netty.seri.MessageCodecUtil;
import com.github.wxz.rpc.netty.seri.MessageEncoder;
/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class KryoEncoder extends MessageEncoder {

    public KryoEncoder(MessageCodecUtil util) {
        super(util);
    }
}

