package com.github.wxz.rpc.netty.serialize.protostuff;

import com.github.wxz.rpc.netty.serialize.MessageCodecUtil;
import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffCodecUtil implements MessageCodecUtil {
    private static Closer closer = Closer.create();
    private ProtoStuffSerializePool pool = ProtoStuffSerializePool.getProtostuffPoolInstance();
    private boolean rpcDirect = false;

    public boolean isRpcDirect() {
        return rpcDirect;
    }

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }

    @Override
    public void encode(final ByteBuf out, final Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            ProtoStuffSerialize protoStuffSerialization = pool.borrow();
            protoStuffSerialization.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
            pool.restore(protoStuffSerialization);
        } finally {
            closer.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            ProtoStuffSerialize protoStuffSerialization = pool.borrow();
            protoStuffSerialization.setRpcDirect(rpcDirect);
            Object obj = protoStuffSerialization.deserialize(byteArrayInputStream);
            pool.restore(protoStuffSerialization);
            return obj;
        } finally {
            closer.close();
        }
    }
}

