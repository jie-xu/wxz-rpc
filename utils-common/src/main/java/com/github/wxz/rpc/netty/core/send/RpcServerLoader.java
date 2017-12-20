package com.github.wxz.rpc.netty.core.send;

import com.github.wxz.RpcSystemConfig;
import com.github.wxz.rpc.netty.parallel.RpcThreadPool;
import com.github.wxz.rpc.netty.seri.RpcSerializeProtocol;
import com.google.common.util.concurrent.*;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
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

    private static int threadNums = RpcSystemConfig.SYSTEM_PROPERTY_THREAD_POOL_THREAD_NUMS;
    private static int queueNums = RpcSystemConfig.SYSTEM_PROPERTY_THREAD_POOL_QUEUE_NUMS;

    private static ListeningExecutorService threadPoolExecutor =
            MoreExecutors.listeningDecorator(
                    (ThreadPoolExecutor) RpcThreadPool.getExecutor(threadNums, queueNums));

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(PARALLEL);

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
        String[] ipAddress = serverAddress.split(RpcServerLoader.DELIMITER);
        if (ipAddress.length == RpcSystemConfig.IP_ADDRESS_PORT_ARRAY_LENGTH) {
            String host = ipAddress[0];
            int port = Integer.parseInt(ipAddress[1]);
            final InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
            LOGGER.info("----------------  rpc client start success ..address {}, post {} --------------", host, port);
            ListenableFuture<Boolean> listenableFuture =
                    threadPoolExecutor.submit(new MsgSendInitializeTask(eventLoopGroup, remoteAddress, serializeProtocol));
            Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
                @Override
                public void onSuccess(@Nullable Boolean result) {
                    try {
                        lock.lock();
                        if (msgSendHandler == null) {
                            handlerStatus.await();
                        }
                        if (result.equals(Boolean.TRUE) && msgSendHandler != null) {
                            connectStatus.signalAll();
                        }
                    } catch (InterruptedException ex) {
                        LOGGER.error("exception", ex.getMessage(), ex);
                    } finally {
                        lock.unlock();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    LOGGER.error("", throwable);
                }
            }, threadPoolExecutor);
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
