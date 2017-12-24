package com.github.wxz.rpc.log;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2017/5/6.
 *
 * @author zhangwenqiang
 */
public class XzLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(XzLogger.class);

    public static void debug(XzLog xzLog) {
        if (xzLog != null) {
            LOGGER.debug(JSON.toJSONString(xzLog));
        }
    }

    public static void info(XzLog xzLog) {
        if (xzLog != null) {
            LOGGER.info(JSON.toJSONString(xzLog));
        }
    }

    public static void error(XzLog xzLog) {
        if (xzLog != null) {
            LOGGER.error(JSON.toJSONString(xzLog));
        }
    }
}
