package com.github.wxz.rpc.filter.support;

import com.github.wxz.rpc.filter.Filter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:20
 */
public class SimpleFilter implements Filter {
    @Override
    public boolean before(Method method, Object processor, Object[] requestObjects) {
        System.out.println(StringUtils.center("[SimpleFilter##before]", 48, "*"));
        return true;
    }

    @Override
    public void after(Method method, Object processor, Object[] requestObjects) {
        System.out.println(StringUtils.center("[SimpleFilter##after]", 48, "*"));
    }
}

