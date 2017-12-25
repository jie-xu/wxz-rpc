package com.github.wxz.core.rpc.core.invoke;


import com.github.wxz.core.config.RpcSystemConfig;
import com.github.wxz.core.rpc.core.invoke.hash.HashMsgRevInitializeTask;
import com.github.wxz.core.rpc.model.MsgRequest;
import com.github.wxz.core.rpc.model.MsgResponse;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -13:14
 */
public class RevInitTaskFacade {
    private MsgRequest msgRequest;
    private MsgResponse msgResponse;
    private Map<String, Object> handlerMap;

    private boolean isMetrics = RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT;
    private boolean jmxMetricsHash = RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_SUPPORT;


    public RevInitTaskFacade(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        this.msgRequest = msgRequest;
        this.msgResponse = msgResponse;
        this.handlerMap = handlerMap;
    }

    /**
     * isMetrics ?
     * @return
     */
    public Callable<Boolean> getTask() {
        return isMetrics ? getMetricsTask():new MsgRevInitTaskAdapter(msgRequest, msgResponse, handlerMap);
    }

    /**
     * hash or not
     * @return
     */
    private Callable<Boolean> getMetricsTask() {
        return jmxMetricsHash ? new HashMsgRevInitializeTask(msgRequest, msgResponse, handlerMap) : new MsgRevInitializeTask(msgRequest, msgResponse, handlerMap);
    }
}
