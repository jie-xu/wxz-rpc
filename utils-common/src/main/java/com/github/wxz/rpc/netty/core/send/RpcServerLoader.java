package com.github.wxz.rpc.netty.core.send;

import com.github.wxz.RpcSystemConfig;
import com.github.wxz.rpc.netty.core.ExecutorManager;
import com.github.wxz.rpc.netty.seri.RpcSerializeProtocol;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -11:25
 */
public class RpcServerLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerLoader.class);
    private static final String DELIMITER = RpcSystemConfig.DELIMITER;

    private static final int PARALLEL = RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL * 2;
    private static volatile RpcServerLoader rpcServerLoader = null;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(PARALLEL);

    private static int threadNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS;
    private static int queueNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS;

    private static ListeningExecutorService threadPoolExecutor =
           null;// MoreExecutors.listeningDecorator((ThreadPoolExecutor)
                  //  RpcThreadPool.getExecutor(threadNums, queueNums));

    private Lock lock = new ReentrantLock();
    private Condition connectStatus = lock.newCondition();
    private Condition handlerStatus = lock.newCondition();
    private MsgSendHandler msgSendHandler = null;

    private RpcServerLoader() {

    }

    /**
     * 单例
     *
     * @return
     */
    public static RpcServerLoader getInstance() {
        if (rpcServerLoader == null) {
            synchronized (RpcServerLoader.class) {
                if (rpcServerLoader == null) {
                    rpcServerLoader = new RpcServerLoader();
                }
            }
        }
        return rpcServerLoader;
    }

    public void load(String serverAddress, RpcSerializeProtocol serializeProtocol) {
        String[] ipAddr = serverAddress.split(RpcServerLoader.DELIMITER);
        if (ipAddr.length == RpcSystemConfig.IP_ADDRESS_PORT_ARRAY_LENGTH) {
            String host = ipAddr[0];
            int port = Integer.parseInt(ipAddr[1]);
            final InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
            LOGGER.info("rpc client start success ..address {}, post {}", host, port);
            ExecutorManager.execute(new MsgSendInitializeTask(eventLoopGroup, remoteAddress, serializeProtocol));
        }
    }

    public void unLoad() {
        msgSendHandler.close();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    public void setMessageSendHandler(MsgSendHandler msgSendHandler) {
        try {
            lock.lock();
            this.msgSendHandler = msgSendHandler;
            handlerStatus.signal();
        } finally {
            lock.unlock();
        }
    }
}
