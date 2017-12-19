package com.github.wxz.rpc.netty.core;

import com.github.wxz.RpcSystemConfig;
import com.github.wxz.rpc.netty.parallel.NamedThreadFactory;
import com.github.wxz.rpc.netty.resolver.ApiEchoResolver;
import com.github.wxz.rpc.netty.seri.RpcSerializeProtocol;
import com.google.common.util.concurrent.ListeningExecutorService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.nio.channels.spi.SelectorProvider;
import java.util.Map;
import java.util.concurrent.*;

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
    private int numberOfEchoThreadsPool = 1;
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
                    msgRecvExecutor = new MsgRecvExecutor();
                }
            }
        }
        return msgRecvExecutor;
    }

    public void start() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new MsgRecvChannelInitializer(handlerMap)
                            .buildRpcSerializeProtocol(serializeProtocol))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            String[] ipAddr = serverAddress.split(MsgRecvExecutor.DELIMITER);
            if (ipAddr.length == RpcSystemConfig.IP_ADDRESS_PORT_ARRAY_LENGTH) {
                final String host = ipAddr[0];
                final int port = Integer.parseInt(ipAddr[1]);
                ChannelFuture future = null;
                future = bootstrap.bind(host, port).sync();

                future.addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            final ExecutorService executor = Executors.newFixedThreadPool(numberOfEchoThreadsPool);
                            ExecutorCompletionService<Boolean> completionService = new ExecutorCompletionService<>(executor);
                            completionService.submit(new ApiEchoResolver(host, echoApiPort));

                            System.out.printf("rpc server start success!\nip:%s\nport:%d\nprotocol:%s\nstart-time:%s\njmx-invoke-metrics:%s\n\n",
                                    host, port,
                                    serializeProtocol,
                                    "",//ModuleMetricsHandler.getStartTime(),
                                    (RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT ? "open" : "close"));

                            channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(final  ChannelFuture future) throws Exception {
                                    executor.shutdownNow();
                                }
                            });
                        }
                    }
                });


            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        worker.shutdownGracefully();
        boss.shutdownGracefully();
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
