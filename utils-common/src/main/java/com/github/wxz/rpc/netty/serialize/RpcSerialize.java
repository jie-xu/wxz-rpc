package com.github.wxz.rpc.netty.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public interface RpcSerialize {
    /**
     * serialize
     *
     * @param output
     * @param object
     * @throws IOException
     */
    void serialize(OutputStream output, Object object) throws IOException;

    /**
     * deserialize
     *
     * @param input
     * @return
     * @throws IOException
     */
    Object deserialize(InputStream input) throws IOException;
}

