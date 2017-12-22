package com.github.wxz.rpc.netty.core.invoke;

import com.alibaba.fastjson.JSON;
import com.github.wxz.rpc.config.RpcSystemConfig;
import com.github.wxz.rpc.netty.model.MsgRequest;
import com.github.wxz.rpc.netty.model.MsgResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -13:22
 */
public abstract class AbstractMsgRevInitTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMsgRevInitTask.class);
    protected MsgRequest msgRequest = null;
    protected MsgResponse msgResponse = null;
    protected Map<String, Object> handlerMap = null;

    protected boolean notNull = true;

    protected long invokeTimeStamp;

    public AbstractMsgRevInitTask(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        this.msgRequest = msgRequest;
        this.msgResponse = msgResponse;
        this.handlerMap = handlerMap;
    }

    /**
     * 线程池调用
     * @return
     */
    @Override
    public Boolean call() {
        try {
            //获取资源
            acquire();
            msgResponse.setMessageId(msgRequest.getMessageId());
            //invoke
            injectInvoke();

            Object result = reflect(msgRequest);
            boolean success = ((notNull && result != null) || !notNull);
            if (success) {
                msgResponse.setResult(result);
                msgResponse.setError("");
                msgResponse.setNotNull(notNull);
                injectSuccessInvoke(invokeTimeStamp);
            } else {
                LOGGER.error(RpcSystemConfig.FILTER_RESPONSE_MSG);
                msgResponse.setResult(null);
                msgResponse.setError(RpcSystemConfig.FILTER_RESPONSE_MSG);
                injectFilterInvoke();
            }
            return Boolean.TRUE;
        } catch (Throwable t) {
            msgResponse.setError(JSON.toJSONString(t.getStackTrace()));
            LOGGER.error("RPC Server invoke error!", t);
            injectFailInvoke(t);
            return Boolean.FALSE;
        } finally {
            release();
        }

    }

    /**
     * reflect
     * @param msgRequest
     * @return
     */
    private Object reflect(MsgRequest msgRequest) {
        ProxyFactory proxyFactory = new ProxyFactory(new MethodInvoker());
        NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
        advisor.setMappedName("invoke");
        advisor.setAdvice(new MethodProxyAdvisor(handlerMap));
        proxyFactory.addAdvisor(advisor);
        MethodInvoker methodInvoker = (MethodInvoker) proxyFactory.getProxy();
        Object object = methodInvoker.invoke(msgRequest);
        invokeTimeStamp = methodInvoker.getInvokeTimeStamp();
        setNotNull(((MethodProxyAdvisor) advisor.getAdvice()).isNotNull());
        return object;
    }

    /**
     * invoke
     */
    protected abstract void injectInvoke();

    /**
     * success invoke
     *
     * @param invokeTimeStamp
     */
    protected abstract void injectSuccessInvoke(long invokeTimeStamp);

    /**
     * fail invoke
     *
     * @param error
     */
    protected abstract void injectFailInvoke(Throwable error);

    /**
     * filter 后invoke
     */
    protected abstract void injectFilterInvoke();

    /**
     * 获取资源
     */
    protected abstract void acquire();

    /**
     * 释放资源
     */
    protected abstract void release();

    public MsgRequest getMsgRequest() {
        return msgRequest;
    }

    public void setMsgRequest(MsgRequest msgRequest) {
        this.msgRequest = msgRequest;
    }

    public MsgResponse getMsgResponse() {
        return msgResponse;
    }

    public void setMsgResponse(MsgResponse msgResponse) {
        this.msgResponse = msgResponse;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public long getInvokeTimeStamp() {
        return invokeTimeStamp;
    }

    public void setInvokeTimeStamp(long invokeTimeStamp) {
        this.invokeTimeStamp = invokeTimeStamp;
    }
}
