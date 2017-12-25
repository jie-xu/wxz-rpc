package com.github.wxz.http.model;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * http response
 * @author xianzhi.wang
 * @date 2017/12/24 -13:15
 */
public class ApiHttpResponse {

    private HttpResponseStatus httpResponseStatus = HttpResponseStatus.OK;

    private byte[] content;

    public HttpResponseStatus getHttpResponseStatus() {
        return httpResponseStatus;
    }

    public void setHttpResponseStatus(HttpResponseStatus httpResponseStatus) {
        this.httpResponseStatus = httpResponseStatus;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
