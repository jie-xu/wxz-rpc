package com.github.wxz.rpc.netty.core;

import com.github.wxz.rpc.netty.seri.RpcSerializeProtocol;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -11:25
 */
public class RpcServerLoader {

    private static volatile RpcServerLoader rpcServerLoader = null;
    private Lock lock = new ReentrantLock();
    private Condition connectStatus = lock.newCondition();
    private Condition handlerStatus = lock.newCondition();

    private RpcServerLoader() {

    }

    public void load(String serverAddress, RpcSerializeProtocol serializeProtocol) {

    }

    public void unLoad() {
//        messageSendHandler.close();
//        threadPoolExecutor.shutdown();
//        eventLoopGroup.shutdownGracefully();
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
}
