package com.github.wxz.core.zk.store;

import java.io.Closeable;
import java.util.List;

/**
 * @author xianzhi.wang
 * @date 2017/12/25 -14:44
 */
public interface TreePathStore extends Closeable {

    /**
     * 定义一个根目录。
     *
     * @return
     */
    String getRootPath();


    /**
     * put
     *
     * @param path
     * @param data
     */
    void put(String path, String data);

    /**
     * exist
     *
     * @param path
     * @return
     */
    boolean exist(String path);

    /**
     * delete
     *
     * @param path
     */
    void delete(String path);

    /**
     * get
     *
     * @param path
     * @return
     */
    String get(String path);

    /**
     * listChildPaths
     *
     * @param path
     * @return
     */
    List<String> listChildPaths(String path);

    /**
     * 添加触发器，当root下，有值发生变化的时候，会触发
     *
     * @param valueChangeListener
     */
    void addValueChangeListener(ValueChangeListener valueChangeListener);
}
