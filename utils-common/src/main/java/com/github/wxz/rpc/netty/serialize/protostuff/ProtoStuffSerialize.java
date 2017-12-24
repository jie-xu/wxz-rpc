package com.github.wxz.rpc.netty.serialize.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.github.wxz.rpc.model.MsgRequest;
import com.github.wxz.rpc.model.MsgResponse;
import com.github.wxz.rpc.netty.serialize.RpcSerialize;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffSerialize implements RpcSerialize {
    private static SchemaCache cachedSchema = SchemaCache.getInstance();
    private static Objenesis objenesis = new ObjenesisStd(true);
    private boolean rpcDirect = false;

    private static <T> Schema<T> getSchema(Class<T> cls) {
        return (Schema<T>) cachedSchema.get(cls);
    }

    public boolean isRpcDirect() {
        return rpcDirect;
    }

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }

    @Override
    public Object deserialize(InputStream input) {
        try {
            Class cls = isRpcDirect() ? MsgRequest.class : MsgResponse.class;
            Object message = objenesis.newInstance(cls);
            Schema<Object> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(input, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void serialize(OutputStream output, Object object) {
        Class cls = object.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = getSchema(cls);
            ProtostuffIOUtil.writeTo(output, object, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }
}

