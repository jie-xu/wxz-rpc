package com.github.wxz.core.rpc.spring;

import com.github.wxz.core.rpc.core.send.ClientStopEvent;
import com.github.wxz.core.rpc.core.send.ClientStopEventListener;
import com.github.wxz.core.rpc.core.send.MsgSendExecutor;
import com.github.wxz.core.rpc.netty.serialize.RpcSerializeProtocol;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 消费者
 *
 * @author xianzhi.wang
 * @date 2017/12/19 -16:04
 */
public class RpcReference implements FactoryBean, InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReference.class);
    private String interfaceName;
    private String ipAddress;
    private String protocol;

    private EventBus eventBus = new EventBus();

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public void destroy() throws Exception {
        eventBus.post(new ClientStopEvent(0));
    }

    @Override
    public Object getObject() throws Exception {
        return MsgSendExecutor.getInstance().execute(getObjectType());
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return this.getClass().getClassLoader().loadClass(interfaceName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("spring analyze fail!", e);
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //开启rpc client
        MsgSendExecutor.getInstance().setRpcServerLoader(ipAddress, RpcSerializeProtocol.valueOf(protocol));

        ClientStopEventListener listener = new ClientStopEventListener();
        eventBus.register(listener);
    }
}
