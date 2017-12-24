package com.github.wxz.rpc.netty.resolver;

import com.alibaba.fastjson.JSON;
import com.github.wxz.rpc.api.Api;
import com.github.wxz.rpc.api.ApiManager;
import com.github.wxz.rpc.model.ApiHttpRequest;
import com.github.wxz.rpc.model.ApiHttpResponse;
import com.github.wxz.rpc.model.JsonResult;
import com.github.wxz.rpc.model.RequestContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * TEXT:: abstract 类 注入会失败
 *
 * @author xianzhi.wang
 * @date 2017/12/24 -13:14
 */
@Component
public class HttpCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpCallback.class);

    public ApiHttpResponse handle(ApiHttpRequest apiHttpRequest) {

        ApiHttpResponse apiHttpResponse = new ApiHttpResponse();
        LOGGER.info("apiHttpRequest {}", JSON.toJSONString(apiHttpRequest));
        if (apiHttpRequest == null) {
            JsonResult jsonResult = JsonResult.getInstance(2, "无效的请求");
            apiHttpResponse.setContent(JSON.toJSONBytes(jsonResult));
            return apiHttpResponse;
        }
        String originalContent = null;
        try {
            originalContent = new String(apiHttpRequest.getContent(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestContent requestContent = new RequestContent();
        requestContent.setContent(originalContent);

        Api api = ApiManager.getApi(apiHttpRequest.getPath());
        JsonResult jsonResult = null;

        try {
            jsonResult = api.handle(requestContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        apiHttpResponse.setContent(JSON.toJSONBytes(jsonResult));
        return apiHttpResponse;
    }
}
