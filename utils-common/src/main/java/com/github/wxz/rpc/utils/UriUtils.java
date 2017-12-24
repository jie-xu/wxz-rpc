package com.github.wxz.rpc.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -13:53
 */
public class UriUtils {
    private static final String D = "/";

    /**
     * 返回请求uri的path
     */
    public static String parsePath(String uri) {
        if (uri == null || uri.length() < 1 || !uri.startsWith(D)) {
            return "";
        }
        int index = uri.indexOf("?");
        index = index < 0 ? uri.length() : index;
        return uri.substring(0, index);
    }

    /**
     * 返回请求uri的参数列表
     */
    public static Map<String, Object> parseParam(String uri) {
        //至少4个才会产生参数  /?x=
        Map<String, Object> map = new HashMap<>();
        if (uri == null || uri.length() < 4 || !uri.startsWith(D)) {
            return map;
        }
        int index = uri.indexOf("?");
        if (index <= 0 || index == uri.length() - 1) {
            return map;
        }
        String str = uri.substring(index + 1);
        String[] params = str.split("&");
        for (String param : params) {
            if (param == null || param.length() < 2) {
                continue;
            }
            int paramIndex = param.indexOf("=");
            if (paramIndex <= 0) {
                continue;
            }
            String key = param.substring(0, paramIndex);
            String value = param.substring(paramIndex + 1);
            map.put(key.trim(), value.trim());
        }
        return map;
    }
}
