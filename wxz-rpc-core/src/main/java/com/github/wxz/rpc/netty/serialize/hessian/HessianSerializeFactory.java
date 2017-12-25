package com.github.wxz.rpc.netty.serialize.hessian;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class HessianSerializeFactory extends BasePooledObjectFactory<HessianSerialize> {

    @Override
    public HessianSerialize create() throws Exception {
        return createHessian();
    }

    @Override
    public PooledObject<HessianSerialize> wrap(HessianSerialize hessian) {
        return new DefaultPooledObject<HessianSerialize>(hessian);
    }

    private HessianSerialize createHessian() {
        return new HessianSerialize();
    }
}

