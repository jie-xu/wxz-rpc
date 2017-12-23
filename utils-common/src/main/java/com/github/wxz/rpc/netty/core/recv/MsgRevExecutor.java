package com.github.wxz.rpc.netty.core.recv;

import com.github.wxz.rpc.ability.AbilityDetailProvider;
import com.github.wxz.rpc.config.RpcSystemConfig;
import com.github.wxz.rpc.jmx.ModuleMetricsHandler;
import com.github.wxz.rpc.parallel.ExecutorManager;
import com.github.wxz.rpc.netty.core.MsgChannelInitializer;
import com.github.wxz.rpc.netty.handler.HandlerType;
import com.github.wxz.rpc.netty.resolver.ApiEchoResolver;
import com.github.wxz.rpc.netty.serialize.RpcSerializeProtocol;
import com.github.wxz.rpc.parallel.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MsgRevExecutor extends Thread implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgRevExecutor.class);

    private static final int PARALLEL = RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL;

    private static volatile MsgRevExecutor msgRevExecutor = null;

    /**
     * 是否开启http
     */
    private static boolean HTTP_FLAG = true;

    ThreadFactory threadRpcFactory = new NamedThreadFactory("rpc ThreadFactory");

    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup(
            PARALLEL, threadRpcFactory,
            SelectorProvider.provider());

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
    private MsgRevExecutor() {
        handlerMap.clear();
        register();
    }

    /**
     * 单例
     *
     * @return
     */
    public static MsgRevExecutor getInstance() {
        if (msgRevExecutor == null) {
            synchronized (MsgRevExecutor.class) {
                if (msgRevExecutor == null) {
                    msgRevExecutor = new MsgRevExecutor();
                }
            }
        }
        return msgRevExecutor;
    }


    /**
     * shutDown
     */
    public void shutDown() {
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }

    @Override
    public void run() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new MsgChannelInitializer(handlerMap)
                    .buildRpcSerializeProtocol(serializeProtocol, HandlerType.REC));
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            String[] ipAddress = serverAddress.split(RpcSystemConfig.DELIMITER);
            if (ipAddress.length == RpcSystemConfig.IP_ADDRESS_PORT_ARRAY_LENGTH) {
                final String host = ipAddress[0];
                final int port = Integer.parseInt(ipAddress[1]);
                ChannelFuture future = bootstrap.bind(host, port).sync();
                if (HTTP_FLAG) {
                    //开启新的线程,apiEcho
                    ExecutorManager.execute(new ApiEchoResolver(host, echoApiPort));
                    LOGGER.info("rpc server execute success,ip: {} port: {} protocol: {}execute-time: {} jmx-invoke-metrics: {}",
                            host, port,
                            serializeProtocol,
                            ModuleMetricsHandler.getStartTime(),
                            (RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT ? "open" : "close"));
                    future.channel().closeFuture().sync();
                }
            } else {
                LOGGER.info("rpc Server execute fail!");
            }

        } catch (InterruptedException e) {
            LOGGER.error("rpc Server execute fail!");
        }
    }


    private void register() {
        //TODO
        //handlerMap.put(RpcSystemConfig.RPC_COMPILER_SPI_ATTR, new AccessAdaptiveProvider());
        handlerMap.put(RpcSystemConfig.RPC_ABILITY_DETAIL_SPI_ATTR, new AbilityDetailProvider());
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
