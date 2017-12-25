
package com.github.wxz.core.rpc.netty.serialize.protostuff;


import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class ProtoStuffSerializeFactory extends BasePooledObjectFactory<ProtoStuffSerialize> {

    @Override
    public ProtoStuffSerialize create() throws Exception {
        return createProtostuff();
    }

    @Override
    public PooledObject<ProtoStuffSerialize> wrap(ProtoStuffSerialize protostuff) {
        return new DefaultPooledObject<ProtoStuffSerialize>(protostuff);
    }

    private ProtoStuffSerialize createProtostuff() {
        return new ProtoStuffSerialize();
    }
}
