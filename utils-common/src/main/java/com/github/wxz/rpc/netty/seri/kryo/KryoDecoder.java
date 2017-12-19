
package com.github.wxz.rpc.netty.seri.kryo;


import com.github.wxz.rpc.netty.seri.MessageCodecUtil;
import com.github.wxz.rpc.netty.seri.MessageDecoder;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class KryoDecoder extends MessageDecoder {

    public KryoDecoder(MessageCodecUtil util) {
        super(util);
    }
}
