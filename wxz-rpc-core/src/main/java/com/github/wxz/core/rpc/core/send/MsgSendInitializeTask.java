package com.github.wxz.core.rpc.core.send;

import com.github.wxz.core.rpc.core.load.RpcServerLoader;
import com.github.wxz.core.rpc.handler.HandlerType;
import com.github.wxz.core.rpc.handler.MsgChannelInitializer;
import com.github.wxz.core.rpc.netty.serialize.RpcSerializeProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -17:12
 */
public class MsgSendInitializeTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgSendInitializeTask.class);
    private EventLoopGroup eventLoopGroup = null;
    private InetSocketAddress serverAddress = null;
    private RpcSerializeProtocol protocol;

    public MsgSendInitializeTask(EventLoopGroup eventLoopGroup, InetSocketAddress serverAddress, RpcSerializeProtocol protocol) {
        this.eventLoopGroup = eventLoopGroup;
        this.serverAddress = serverAddress;
        this.protocol = protocol;
    }

    @Override
    public Boolean call() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.remoteAddress(serverAddress);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(
                    new MsgChannelInitializer().
                            buildRpcSerializeProtocol(protocol, HandlerType.SEND));
            ChannelFuture channelFuture = bootstrap.connect().sync();
            LOGGER.info("----------------  rpc client execute success .. address {}, post {} --------------",
                    serverAddress.getHostName(), serverAddress.getPort());
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        MsgSendHandler msgSendHandler = future.channel().pipeline().get(MsgSendHandler.class);
                        RpcServerLoader.getInstance().setMsgSendHandler(msgSendHandler);
                    } else {
                        LOGGER.error("future is not success ...");
                        //重试1次
                        //call();
                    }
                }
            });
            channelFuture.channel().pipeline().get(MsgSendHandler.class);
            return true;
        } catch (Exception e) {
            LOGGER.error("start fail ...", e);
            return false;
        }
    }
}
