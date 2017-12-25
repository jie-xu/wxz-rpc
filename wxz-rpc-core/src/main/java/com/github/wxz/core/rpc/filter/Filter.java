package com.github.wxz.core.rpc.filter;

import java.lang.reflect.Method;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:20
 */
public interface Filter {
    /**
     * If returns false, indicates that the RPC request method is intercept.
     *
     * @param method
     * @param processor
     * @param requestObjects
     * @return
     */
    boolean before(Method method, Object processor, Object[] requestObjects);

    /**
     * If filter's before function returns false, filter's after function will not be called.
     *
     * @param method
     * @param processor
     * @param requestObjects
     */
    void after(Method method, Object processor, Object[] requestObjects);
}

