package com.github.wxz.rpc.netty.resolver;

import com.github.wxz.rpc.ability.AbilityDetailProvider;
import com.github.wxz.rpc.config.HttpServerConfig;
import com.github.wxz.rpc.config.RpcSystemConfig;
import com.github.wxz.rpc.jmx.ModuleMetricsProcessor;
import com.github.wxz.rpc.log.XzLog;
import com.github.wxz.rpc.log.XzLogger;
import com.github.wxz.rpc.model.ApiHttpRequest;
import com.github.wxz.rpc.model.ApiHttpResponse;
import com.github.wxz.rpc.utils.UriUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -17:11
 */
public class ApiEchoHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApiEchoHandler.class);

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONNECTION = "Connection";
    private static final String KEEP_ALIVE = "keep-alive";

    private static final String METRICS = "metrics";
    private static final String METRICS_ERR_MSG = "rpc rpc.jmx.invoke.metrics attribute is closed!";

    private HttpCallback httpCallback;

    private HttpServerConfig httpServerConfig;

    public ApiEchoHandler(HttpCallback httpCallback, HttpServerConfig httpServerConfig) {
        this.httpCallback = httpCallback;
        this.httpServerConfig = httpServerConfig;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //超时断开连接
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
        }
    }

    /**
     * 返回页面信息
     *
     * @param ctx
     * @param httpObject
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) {
        if (!(httpObject instanceof FullHttpRequest)) {
            LOGGER.info("httpObject is  not FullHttpRequest");
            return;
        }
        FullHttpRequest httpRequest = (FullHttpRequest) httpObject;

        //请求图标过滤
        if (RpcSystemConfig.FAVICON_ICO.equals(httpRequest.uri())) {
            return;
        }

        LOGGER.info("rev path:{}", httpRequest.uri());

        ApiHttpRequest apiHttpRequest = new ApiHttpRequest();
        String clientIp = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        apiHttpRequest.setClientIp(clientIp);
        apiHttpRequest.setHttpMethod(httpRequest.method());
        apiHttpRequest.setPath(UriUtils.parsePath(httpRequest.uri()));
        apiHttpRequest.setArgs(UriUtils.parseParam(httpRequest.uri()));

        ByteBuf byteBuf = Unpooled.copiedBuffer(httpRequest.content());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("rev content:{}", new String(byteBuf.array()));
        }

        apiHttpRequest.setContent(byteBuf.array());

        boolean isKeepAlive = HttpUtil.isKeepAlive(httpRequest);

        ApiHttpResponse apiHttpResponse = httpCallback.handle(apiHttpRequest);

        if (apiHttpResponse != null) {
            try {
                byte[] contentBytes = apiHttpResponse.getContent() == null ?
                        "".getBytes("utf-8") :
                        apiHttpResponse.getContent();
                //记录请求的错误日志
                if (httpServerConfig.getIsStoreErrorRequestLog()
                        && apiHttpResponse.getHttpResponseStatus() != HttpResponseStatus.OK) {
                    XzLog xzLog = new XzLog();
                    xzLog.setSysNo(httpServerConfig.getSysNo());
                    xzLog.setClientIp(apiHttpRequest.getClientIp());
                    xzLog.setErrorMsg(new String(contentBytes, "utf-8"));
                    xzLog.setLogLevel("error");
                    xzLog.setLogType("AccessLog");
                    xzLog.setResult("-1");
                    XzLogger.error(xzLog);
                }
                FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        apiHttpResponse.getHttpResponseStatus(),
                        Unpooled.wrappedBuffer(contentBytes));
                fullHttpResponse.headers().set(
                        HttpHeaderNames.CONTENT_TYPE,
                        "text/plain;charset=UTF-8");
                if (httpServerConfig.getIsResponseGzip()) {
                    fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_ENCODING, "gzip");
                }
                fullHttpResponse.headers().set(HttpHeaderNames.SERVER, "wxz");
                fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                        fullHttpResponse.content().readableBytes());
                if (!isKeepAlive) {
                    ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    fullHttpResponse.headers().set(HttpHeaderNames.CONNECTION,
                            HttpHeaderValues.KEEP_ALIVE);
                    ctx.writeAndFlush(fullHttpResponse);
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("exception", e);
            }


        }
//
//        if (httpObject instanceof HttpRequest) {
//            HttpRequest req = (HttpRequest) httpObject;
//            byte[] content = buildResponseMsg(req);
//            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));
//            response.headers().set(CONTENT_TYPE, "text/html");
//            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
//            response.headers().set(CONNECTION, KEEP_ALIVE);
//            ctx.write(response);
//        }
    }


    /**
     * 返回
     *
     * @param req
     * @return
     */
    private byte[] buildResponseMsg(HttpRequest req) {
        byte[] content = null;
        boolean metrics = (req.uri().indexOf(METRICS) != -1);
        if (RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT && metrics) {
            try {
                content = ModuleMetricsProcessor.getInstance().buildModuleMetrics().getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (!RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT && metrics) {
            content = METRICS_ERR_MSG.getBytes();
        } else {
            AbilityDetailProvider provider = new AbilityDetailProvider();
            content = provider.listAbilityDetail(true).toString().getBytes();
        }
        return content;
    }
}

