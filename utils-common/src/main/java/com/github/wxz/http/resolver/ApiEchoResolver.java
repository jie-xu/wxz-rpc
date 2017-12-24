package com.github.wxz.http.resolver;

import com.github.wxz.config.HttpServerConfig;
import com.github.wxz.rpc.parallel.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * ApiEchoResolver
 * netty 开启http服务
 *
 * @author xianzhi.wang
 * @date 2017/12/19 -17:11
 */
public class ApiEchoResolver extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiEchoResolver.class);
    private HttpServerConfig httpServerConfig;
    private HttpCallback httpCallback;


    public ApiEchoResolver(HttpServerConfig httpServerConfig, HttpCallback httpCallback) {
        this.setName(ApiEchoResolver.class.getName());
        this.httpServerConfig = httpServerConfig;
        this.httpCallback = httpCallback;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(httpServerConfig.getBossThreadCount());
        EventLoopGroup workerGroup = new NioEventLoopGroup(httpServerConfig.getWorkerThreadCount());
        final EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(httpServerConfig.getBusinessThreadCount(), new NamedThreadFactory("businessGroup"));
        try {
            SslContext sslContext = null;
            if (httpServerConfig.getIsEnableSsl()) {
                //自签证书
                if (httpServerConfig.getIsSelfSignedCertificate()) {
                    SelfSignedCertificate ssc = new SelfSignedCertificate(httpServerConfig.getSelfSignedCertificateDomain());
                    sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
                }
                //签名证书
                else {
                    InputStream keyCertChainInputStream = new FileInputStream(httpServerConfig.getSslKeyCertChainFile());
                    InputStream keyInputStream = new FileInputStream(httpServerConfig.getSslKeyFile());
                    sslContext = SslContextBuilder.forServer(keyCertChainInputStream, keyInputStream, httpServerConfig.getSslKeyPassword()).build();
                    keyCertChainInputStream.close();
                    keyInputStream.close();
                }
            }

            ServerBootstrap bootStrap = new ServerBootstrap();
            bootStrap.option(ChannelOption.SO_BACKLOG, httpServerConfig.getSoBackLog());
            bootStrap.childOption(ChannelOption.TCP_NODELAY, httpServerConfig.getTcpNoDelay());
            bootStrap.group(bossGroup, workerGroup);
            bootStrap.channel(NioServerSocketChannel.class);
            bootStrap.handler(new LoggingHandler(httpServerConfig.getLogLevel()));
            bootStrap.childHandler(new ApiEchoInitializer(businessGroup, httpServerConfig, httpCallback, sslContext));

            Channel ch = bootStrap.bind(httpServerConfig.getPort()).sync().channel();
            LOGGER.info("You can open your web browser see rpc server api interface: " +
                    (httpServerConfig.getIsEnableSsl() ? "https" : "http") + "://" + " host " + ":" + httpServerConfig.getPort() + "/rpc");
            LOGGER.info("You can open your web browser see rpc server metrics http://" + " host " + ":" + httpServerConfig.getPort() + "/rpc/metrics");
            ch.closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("exception  {}", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

