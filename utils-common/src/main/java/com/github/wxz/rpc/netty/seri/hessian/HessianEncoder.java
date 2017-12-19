package com.github.wxz.rpc.netty.seri.hessian;


import com.github.wxz.rpc.netty.seri.MessageCodecUtil;
import com.github.wxz.rpc.netty.seri.MessageEncoder;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class HessianEncoder extends MessageEncoder {

    public HessianEncoder(MessageCodecUtil util) {
        super(util);
    }
}

