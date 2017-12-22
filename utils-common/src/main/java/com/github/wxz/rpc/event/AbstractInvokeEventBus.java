package com.github.wxz.rpc.event;


import com.github.wxz.rpc.jmx.ModuleMetricsHandler;

import javax.management.Notification;

/**
 * @author xianzhi.wang
 * @date 2017/12/22 -16:20
 */
public abstract class AbstractInvokeEventBus {

    protected String moduleName;
    protected String methodName;
    protected ModuleMetricsHandler handler;
    public enum ModuleEvent {
        INVOKE_EVENT,
        INVOKE_SUCCESS_EVENT,
        INVOKE_TIMESTAMP_EVENT,
        INVOKE_MAX_TIMESTAMP_EVENT,
        INVOKE_MIN_TIMESTAMP_EVENT,
        INVOKE_FILTER_EVENT,
        INVOKE_FAIL_EVENT,
        INVOKE_FAIL_STACKTRACE_EVENT
    }

    public AbstractInvokeEventBus() {

    }

    public AbstractInvokeEventBus(String moduleName, String methodName) {
        this.moduleName = moduleName;
        this.methodName = methodName;
    }

    public abstract Notification buildNotification(Object oldValue, Object newValue);

    public void notify(Object oldValue, Object newValue) {
        Notification notification = buildNotification(oldValue, newValue);
        handler.sendNotification(notification);
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ModuleMetricsHandler getHandler() {
        return handler;
    }

    public void setHandler(ModuleMetricsHandler handler) {
        this.handler = handler;
    }




}

