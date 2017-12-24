package com.github.wxz.rpc.api;

import com.github.wxz.rpc.model.JsonResult;
import com.github.wxz.rpc.model.RequestContent;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -15:33
 */
public interface Api {

    /**
     * 处理请求
     *
     * @param requestContent 请求内容
     * @return 处理完成的接口
     * @throws Exception 处理异常
     */
    public JsonResult handle(RequestContent requestContent) throws Exception;
}
