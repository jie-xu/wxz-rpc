package com.github.wxz.core.zk.store;

/**
 * @author xianzhi.wang
 * @date 2017/12/25 -14:46
 */
public interface ValueChangeListener {
     void call(String path ,String value);
}
