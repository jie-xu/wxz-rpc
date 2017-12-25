package com.github.wxz.core.rpc.netty.serialize;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public interface MessageCodecUtil {

    public  final static int MESSAGE_LENGTH = 4;

    /**
     * encode
     *
     * @param out
     * @param message
     * @throws IOException
     */
    void encode(final ByteBuf out, final Object message) throws IOException;

    /**
     * decode
     *
     * @param body
     * @return
     * @throws IOException
     */
    Object decode(byte[] body) throws IOException;
}
