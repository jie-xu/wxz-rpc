package com.github.wxz.http.resolver;

import com.github.wxz.core.config.HttpServerConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import javax.net.ssl.SSLEngine;
import java.util.concurrent.TimeUnit;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -17:11
 */
public class ApiEchoInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;
    private final EventExecutorGroup businessGroup;
    private final HttpServerConfig httpServerConfig;
    private final HttpCallback httpCallback;

    public ApiEchoInitializer(EventExecutorGroup businessGroup,
                              HttpServerConfig httpServerConfig,
                              HttpCallback httpCallback,
                              SslContext sslContext) {
        this.businessGroup = businessGroup;
        this.sslContext = sslContext;
        this.httpCallback = httpCallback;
        this.httpServerConfig = httpServerConfig;
    }

    @Override
    public void initChannel(SocketChannel socketChannel) {

        ChannelPipeline pipeline = socketChannel.pipeline();

        if (httpServerConfig.getIsEnableSsl()) {
            SSLEngine sslEngine;
            if(sslContext!=null){
                sslEngine = sslContext.newEngine(socketChannel.alloc());
                sslEngine.setUseClientMode(false);
                sslEngine.setNeedClientAuth(false);
                pipeline.addLast(new SslHandler(sslEngine));
            }
        }

        pipeline.addLast(new HttpServerCodec());

        pipeline.addLast(new LoggingHandler(httpServerConfig.getLogLevel()));

        //压缩级别
        pipeline.addLast(new HttpContentCompressor(httpServerConfig.getCompressionLevel()));
        //请求最大长度
        pipeline.addLast(new HttpObjectAggregator(httpServerConfig.getMaxContentLength()));

        // 超时检测放到解码器后可以保证是解码成功才记录正常数据的超时判断，防止垃圾数据长连接在线
        pipeline.addLast(new IdleStateHandler(httpServerConfig.getReaderIdleTime(), httpServerConfig.getWriterIdleTime(), 0, TimeUnit.SECONDS));

        pipeline.addLast(businessGroup,new ApiEchoHandler(httpCallback,httpServerConfig));

    }
}

