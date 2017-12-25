package com.github.wxz.rpc.handler;

/**
 * handlerType
 *
 * @author xianzhi.wang
 * @date 2017/12/20 -16:34
 */
public enum HandlerType {
    SEND("send"),

    REC("rec");
    private String handlerType;

    HandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }
}
