package com.github.wxz.rpc.spring;

import com.github.wxz.RpcSystemConfig;
import com.github.wxz.rpc.netty.core.MsgRecvExecutor;
import com.github.wxz.rpc.netty.seri.RpcSerializeProtocol;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:00
 */
public class RpcRegistery implements InitializingBean, DisposableBean {
    private String ipAddr;
    private String protocol;
    private String echoApiPort;
    private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MsgRecvExecutor msgRecvExecutor =  MsgRecvExecutor.getInstance();
        msgRecvExecutor.setServerAddress(ipAddr);
        msgRecvExecutor.setEchoApiPort(Integer.parseInt(echoApiPort));
        msgRecvExecutor.setSerializeProtocol(Enum.valueOf(RpcSerializeProtocol.class, protocol));
        if (RpcSystemConfig.isMonitorServerSupport()) {
            //TODO
        }

//        msgRecvExecu

    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEchoApiPort() {
        return echoApiPort;
    }

    public void setEchoApiPort(String echoApiPort) {
        this.echoApiPort = echoApiPort;
    }
}
