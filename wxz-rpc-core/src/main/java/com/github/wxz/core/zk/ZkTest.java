package com.github.wxz.core.zk;

import com.github.wxz.core.zk.store.TreePathStore;
import com.github.wxz.core.zk.store.ZkTreePathStore;

import java.io.IOException;

/**
 * @author xianzhi.wang
 * @date 2017/12/25 -14:17
 */
public class ZkTest {
    /**
     * zk 连接地址.
     */
    private static final String CONNECTION_URL = "127.0.0.1:2181";
    private static TreePathStore treePathStore;

    public static void main(String[] args) throws IOException {
        treePathStore = new ZkTreePathStore(CONNECTION_URL);
        treePathStore.put("/a", "fdfd");
        treePathStore.close();
        //treePathStore.get("/");
    }
}
