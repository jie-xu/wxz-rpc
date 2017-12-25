package com.github.wxz.core.zk.store;

import com.github.wxz.core.utils.ByteUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xianzhi.wang
 * @date 2017/12/25 -14:52
 */
public class ZkTreePathStore implements TreePathStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkTreePathStore.class);
    private static final String VALUE_CHANGE_LISTENER_NODE = "/change";
    private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
    private CuratorFramework curatorFramework;

    public ZkTreePathStore(String connectString) {
        initCuratorFramework(connectString);
    }

    private void initCuratorFramework(String connectString) {
        try {
            curatorFramework = CuratorFrameworkFactory
                    .builder()
                    .namespace(getRootPath())
                    .connectString(connectString)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .build();
            curatorFramework.start();
            LOGGER.info("start curatorFramework successfully");
        } catch (Exception e) {
            LOGGER.error("zk connect failed,connectString: " + connectString, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getRootPath() {
        return "wxz-rpc";
    }

    @Override
    public void put(String path, String data) {
        byte[] data1 = ByteUtils.toBytes(data);
        try {
            //同步方式创建
            curatorFramework
                    .create()
                    //如果父节点不存在，则自动创建
                    .creatingParentsIfNeeded()
                    //节点模式，持久
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, data1);
            notify(path, data);
        } catch (KeeperException.NodeExistsException e) {
            try {
                curatorFramework
                        .setData()
                        .forPath(path, data1);
                notify(path, data);
            } catch (Exception e1) {
                LOGGER.error("put data error,path: " + path, e1);
            }
        } catch (Exception e) {
            LOGGER.error("put data error,path: " + path, e);
        }

    }

    @Override
    public boolean exist(String path) {
        return false;
    }

    @Override
    public void delete(String path) {

    }

    @Override
    public String get(String path) {
        try {
            byte[] data1 = curatorFramework
                    .getData()
                    .forPath(path);
            return ByteUtils.toString(data1);
        } catch (Exception e) {
            if (e instanceof KeeperException.NoNodeException) {
                return null;
            }
            LOGGER.error("get data error,path: " + path, e);
        }
        return null;
    }

    @Override
    public List<String> listChildPaths(String path) {
        return null;
    }

    @Override
    public void addValueChangeListener(ValueChangeListener valueChangeListener) {

    }

    @Override
    public void close() throws IOException {
        CloseableUtils.closeQuietly(curatorFramework);
    }

    private void notify(String key, String data) {

    }
}
