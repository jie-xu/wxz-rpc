package com.github.wxz.rpc.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -15:38
 */
public class ApiManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiManager.class);
    private static ConcurrentHashMap<String, Api> apiMap = new ConcurrentHashMap<>();

    /**
     * 注册服务
     */
    public static boolean register(String path, Api api) {
        if (path == null || path.isEmpty() || api == null) {
            return false;
        }
        apiMap.putIfAbsent(path, api);
        LOGGER.info("register path {},api {} ", path, api);
        return true;
    }

    /**
     * 获取服务
     */
    public static Api getApi(String path) {
        if (path == null) {
            return null;
        }
        return apiMap.get(path);
    }

}
