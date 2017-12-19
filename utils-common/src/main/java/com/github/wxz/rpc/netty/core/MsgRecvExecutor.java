package com.github.wxz.rpc.netty.core;

import com.github.wxz.RpcSystemConfig;
import com.github.wxz.rpc.netty.parallel.NamedThreadFactory;
import com.github.wxz.rpc.netty.seri.RpcSerializeProtocol;
import com.google.common.util.concurrent.ListeningExecutorService;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.nio.channels.spi.SelectorProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -17:11
 */
public class MsgRecvExecutor implements ApplicationContextAware {

    private static final String DELIMITER = RpcSystemConfig.DELIMITER;
    private static final int PARALLEL = RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL * 2;
    private static int threadNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS;
    private static int queueNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS;

    private static volatile ListeningExecutorService threadPoolExecutor;

    private static volatile MsgRecvExecutor msgRecvExecutor = null;

    ThreadFactory threadRpcFactory = new NamedThreadFactory("rpc ThreadFactory");
    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup(PARALLEL, threadRpcFactory, SelectorProvider.provider());
    private String serverAddress;
    private int echoApiPort;
    private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.JDK_SERIALIZE;





    /**
     * handlerMap
     */
    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();

    /**
     * 构造器
     */
    private MsgRecvExecutor() {
        handlerMap.clear();
        register();
    }

    /**
     * 单例
     *
     * @return
     */
    public static MsgRecvExecutor getInstance() {
        if (msgRecvExecutor == null) {
            synchronized (MsgRecvExecutor.class) {
                if (msgRecvExecutor == null) {
                    return new MsgRecvExecutor();
                }
            }
        }
        return msgRecvExecutor;
    }


    private void register() {
        //TODO
        //handlerMap.put(RpcSystemConfig.RPC_COMPILER_SPI_ATTR, new AccessAdaptiveProvider());
        //handlerMap.put(RpcSystemConfig.RPC_ABILITY_DETAIL_SPI_ATTR, new AbilityDetailProvider());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //TODO
    }


    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getEchoApiPort() {
        return echoApiPort;
    }

    public void setEchoApiPort(int echoApiPort) {
        this.echoApiPort = echoApiPort;
    }

    public RpcSerializeProtocol getSerializeProtocol() {
        return serializeProtocol;
    }

    public void setSerializeProtocol(RpcSerializeProtocol serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    public Map<String, Object> getHandlerMap() {
        return handlerMap;
    }

    public void setHandlerMap(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }
}
