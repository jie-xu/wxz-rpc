package com.github.wxz.rpc.netty.resolver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -17:11
 */
public class ApiEchoResolver implements Runnable {
    private static final boolean SSL = System.getProperty("ssl") != null;
    private String host;
    private int port;

    public ApiEchoResolver(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            SslContext sslCtx = null;
            if (SSL) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            }

            ServerBootstrap bootStrap = new ServerBootstrap();
            bootStrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootStrap.group(bossGroup, workerGroup);
            bootStrap.channel(NioServerSocketChannel.class);
            bootStrap.handler(new LoggingHandler(LogLevel.INFO));
            bootStrap.childHandler(new ApiEchoInitializer(sslCtx));
            Channel ch = bootStrap.bind(port).sync().channel();
            System.out.println("You can open your web browser see rpc server api interface: " +
                    (SSL ? "https" : "http") + "://" + host + ":" + port + "/rpc.html");
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

