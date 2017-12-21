package com.github.wxz.rpc.netty.seri.protostuff;

import com.github.wxz.rpc.config.RpcSystemConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffSerializePool {
    private static volatile ProtoStuffSerializePool poolFactory = null;
    private GenericObjectPool<ProtoStuffSerialize> protoStuffPool;

    private ProtoStuffSerializePool() {
        protoStuffPool = new GenericObjectPool<>(new ProtoStuffSerializeFactory());
    }

    public ProtoStuffSerializePool(final int maxTotal, final int minIdle, final long maxWaitMillis, final long minEvictableIdleTimeMillis) {
        protoStuffPool = new GenericObjectPool<>(new ProtoStuffSerializeFactory());

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        protoStuffPool.setConfig(config);
    }

    public static ProtoStuffSerializePool getProtostuffPoolInstance() {
        if (poolFactory == null) {
            synchronized (ProtoStuffSerializePool.class) {
                if (poolFactory == null) {
                    poolFactory = new ProtoStuffSerializePool(RpcSystemConfig.SERIALIZE_POOL_MAX_TOTAL, RpcSystemConfig.SERIALIZE_POOL_MIN_IDLE, RpcSystemConfig.SERIALIZE_POOL_MAX_WAIT_MILLIS, RpcSystemConfig.SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS);
                }
            }
        }
        return poolFactory;
    }

    public ProtoStuffSerialize borrow() {
        try {
            return getProtoStuffPool().borrowObject();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void restore(final ProtoStuffSerialize object) {
        getProtoStuffPool().returnObject(object);
    }

    public GenericObjectPool<ProtoStuffSerialize> getProtoStuffPool() {
        return protoStuffPool;
    }
}

