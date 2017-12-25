package com.github.wxz.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxz.http.api.Api;
import com.github.wxz.http.api.ApiController;
import com.github.wxz.http.model.JsonResult;
import com.github.wxz.http.model.RequestContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -15:44
 */

@Component
@ApiController("/calculate")
public class CalculateController implements Api {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateController.class);

    @Override
    public JsonResult handle(RequestContent requestContent) throws Exception {
        LOGGER.info(JSON.toJSONString(requestContent));
        return JsonResult.jsonInstance();
    }
}
