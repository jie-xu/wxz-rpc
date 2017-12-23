package com.github.wxz.rpc.netty.core.recv;

import com.github.wxz.rpc.config.RpcSystemConfig;
import com.github.wxz.rpc.netty.core.invoke.RevInitTaskFacade;
import com.github.wxz.rpc.netty.model.MsgRequest;
import com.github.wxz.rpc.netty.model.MsgResponse;
import com.github.wxz.rpc.parallel.ExecutorManager;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -17:11
 */
public class MsgRevHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgRevHandler.class);

    private static int threadNums = RpcSystemConfig.SYSTEM_PROPERTY_THREAD_POOL_THREAD_NUMS;
    private static int queueNums = RpcSystemConfig.SYSTEM_PROPERTY_THREAD_POOL_QUEUE_NUMS;

    private static volatile ThreadPoolExecutor threadPoolExecutor;
    private final Map<String, Object> handlerMap;

    public MsgRevHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
     * 提交任务
     *
     * @param task
     * @param channelHandlerContext
     * @param request
     * @param response
     */
    public static void submit(Callable<Boolean> task,
                              final ChannelHandlerContext channelHandlerContext,
                              final MsgRequest request, final MsgResponse response) {
        if (threadPoolExecutor == null) {
            synchronized (MsgRevExecutor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor =
                            ExecutorManager.getThreadPoolExecutor("MsgRevHandler", threadNums, queueNums);
                }
            }
        }
        ListenableFuture<Boolean> listenableFuture = ExecutorManager.submit(threadPoolExecutor, task);
        //获取回调
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                channelHandlerContext.writeAndFlush(response)
                        .addListener(channel ->
                                LOGGER.info("  rpc server send message-id response:" + request.getMessageId())
                        );
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, threadPoolExecutor);
    }

    /**
     * rpc server rev handle
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MsgRequest msgRequest = (MsgRequest) msg;
        MsgResponse msgResponse = new MsgResponse();

        RevInitTaskFacade facade = new RevInitTaskFacade(msgRequest, msgResponse, handlerMap);
        Callable<Boolean> revTask = facade.getTask();
        //代理对象执行任务
        submit(revTask, ctx, msgRequest, msgResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

