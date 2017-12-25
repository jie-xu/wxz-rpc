package com.github.wxz.core.rpc.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -9:43
 */
public class MsgResponse implements Serializable {

    private String messageId;
    private String error;
    private Object result;
    private boolean notNull;

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

