package com.github.wxz.rpc.netty.seri.hessian;


import com.github.wxz.rpc.netty.seri.MessageCodecUtil;
import com.github.wxz.rpc.netty.seri.MessageDecoder;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class HessianDecoder extends MessageDecoder {

    public HessianDecoder(MessageCodecUtil util) {
        super(util);
    }
}

