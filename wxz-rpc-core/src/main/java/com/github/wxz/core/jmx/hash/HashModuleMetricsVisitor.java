package com.github.wxz.core.jmx.hash;

import com.github.wxz.core.config.RpcSystemConfig;
import com.github.wxz.core.jmx.ModuleMetricsHandler;
import com.github.wxz.core.jmx.ModuleMetricsVisitor;
import com.github.wxz.core.rpc.core.recv.MsgRevExecutor;
import com.github.wxz.core.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xianzhi.wang
 * @date 2017/12/22 -21:11
 */
public class HashModuleMetricsVisitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashModuleMetricsVisitor.class);
    private static volatile HashModuleMetricsVisitor hashModuleMetricsVisitor = null;

    private List<List<ModuleMetricsVisitor>> hashVisitorList = new ArrayList<>();

    private HashModuleMetricsVisitor() {
        init();
    }

    /**
     * instance
     *
     * @return
     */
    public static HashModuleMetricsVisitor getInstance() {
        if (hashModuleMetricsVisitor == null) {
            ReentrantLock reentrantLock = new ReentrantLock();
            try {
                reentrantLock.lock();
                if (hashModuleMetricsVisitor == null) {
                    hashModuleMetricsVisitor = new HashModuleMetricsVisitor();
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        return hashModuleMetricsVisitor;
    }

    public int getHashModuleMetricsVisitorListSize() {
        return hashVisitorList.size();
    }

    /**
     * 初始化JMX数据
     */
    private void init() {
        Map<String, Object> map = MsgRevExecutor.getInstance().getHandlerMap();
        ReflectionUtils utils = new ReflectionUtils();
        if (map.isEmpty() || map.size() == 0) {
            return;
        }

        map.keySet().stream()
                //去除掉无效数据
                .filter(clazz -> StringUtils.isNotEmpty(clazz))
                .filter(clazz -> map.get(clazz) != null)
                .forEach(clazz -> {
                    try {
                        final List<String> list = utils.getClassMethodSignature(Class.forName(clazz));
                        list.stream().forEach(signature -> {
                            List<ModuleMetricsVisitor> visitorList = new ArrayList<>();
                            for (int i = 0; i < RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_NUMS; i++) {
                                ModuleMetricsVisitor visitor = new ModuleMetricsVisitor(clazz, signature);
                                visitor.setHashKey(i);
                                visitorList.add(visitor);
                            }
                            hashVisitorList.add(visitorList);
                        });
                    } catch (ClassNotFoundException e) {
                        LOGGER.error("class not found {}", e);
                    }
                });
    }

    /**
     * 将count值减1
     */
    public void signal() {
        ModuleMetricsHandler.getInstance().getLatch().countDown();
    }

    public List<List<ModuleMetricsVisitor>> getHashVisitorList() {
        return hashVisitorList;
    }

    public void setHashVisitorList(List<List<ModuleMetricsVisitor>> hashVisitorList) {
        this.hashVisitorList = hashVisitorList;
    }
}

