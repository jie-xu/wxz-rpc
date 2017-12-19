package com.github.wxz.rpc.netty.seri;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 序列化方式
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public enum RpcSerializeProtocol {

    JDK_SERIALIZE("jdknative"),
    KRYO_SERIALIZE("kryo"),
    HESSIAN_SERIALIZE("hessian"),
    PROTO_STUFF_SERIALIZE("protostuff");

    private String serializeProtocol;

    private RpcSerializeProtocol(String serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    @Override
    public String toString() {
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toString(this);
    }

    public String getProtocol() {
        return serializeProtocol;
    }
}
