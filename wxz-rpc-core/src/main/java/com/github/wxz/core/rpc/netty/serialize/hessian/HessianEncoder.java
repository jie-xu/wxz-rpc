package com.github.wxz.core.rpc.netty.serialize.hessian;

import com.github.wxz.core.rpc.netty.serialize.MessageCodecUtil;
import com.github.wxz.core.rpc.netty.serialize.MessageEncoder;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class HessianEncoder extends MessageEncoder {

    public HessianEncoder(MessageCodecUtil util) {
        super(util);
    }
}

