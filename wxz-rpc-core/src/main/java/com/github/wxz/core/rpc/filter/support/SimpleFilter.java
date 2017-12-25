package com.github.wxz.core.rpc.filter.support;

import com.github.wxz.core.rpc.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:20
 */
public class SimpleFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFilter.class);

    @Override
    public boolean before(Method method, Object processor, Object[] requestObjects) {
        LOGGER.info("----SimpleFilter before----");
        return true;
    }

    @Override
    public void after(Method method, Object processor, Object[] requestObjects) {
        LOGGER.info("----SimpleFilter after----");
    }
}

