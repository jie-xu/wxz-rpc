package com.github.wxz.rpc.jmx;

import com.github.wxz.rpc.config.RpcSystemConfig;
import com.github.wxz.rpc.netty.core.recv.MsgRevExecutor;
import com.github.wxz.rpc.parallel.ExecutorManager;
import com.github.wxz.rpc.parallel.semaphore.SemaphoreWrapper;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.*;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * ModuleMetricsHandler处理类
 *
 * @author xianzhi.wang
 * @date 2017/12/21 -21:11
 */
public class ModuleMetricsHandler extends AbstractModuleMetricsHandler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleMetricsHandler.class);
    private static volatile ModuleMetricsHandler moduleMetricsHandler = null;
    private String moduleMetricsJmxUrl = "";
    /**
     * 笔记：TODO
     * Semaphore 信号量，
     * Semaphore可以控同时访问的线程个数，
     * 通过 acquire() 获取一个许可，如果没有就等待，
     * 而 release() 释放一个许可
     */
    /**
     * 参数 0 表示许可数目，即同时可以允许0个线程进行访问
     */
    private Semaphore semaphore = new Semaphore(0);

    private SemaphoreWrapper semaphoreWrapper = new SemaphoreWrapper(semaphore);

    private MBeanServerConnection connection;

    /**
     * TODO
     * 笔记：CyclicBarrier 通过它可以实现让一组线程等待至某个状态之后再全部同时执行
     */

    /**
     * CountDownLatch 能够使一个线程等待其他线程完成各自的工作后再执行
     */
    private CountDownLatch latch = new CountDownLatch(1);

    private ModuleMetricsListener listener = new ModuleMetricsListener();

    private ModuleMetricsHandler() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static ModuleMetricsHandler getInstance() {
        if (moduleMetricsHandler == null) {
            synchronized (ModuleMetricsHandler.class) {
                moduleMetricsHandler = new ModuleMetricsHandler();
            }
        }
        return moduleMetricsHandler;
    }

    @Override
    public List<ModuleMetricsVisitor> getModuleMetricsVisitor() {
        return super.getModuleMetricsVisitor();
    }

    @Override
    protected ModuleMetricsVisitor visitCriticalSection(String moduleName, String methodName) {
        final String method = methodName.trim();
        final String module = moduleName.trim();

        //FIXME:
        //JMX度量临界区要注意线程间的并发竞争,否则会统计数据失真
        Iterator iterator = new FilterIterator(visitorList.iterator(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                String statModuleName = ((ModuleMetricsVisitor) object).getModuleName();
                String statMethodName = ((ModuleMetricsVisitor) object).getMethodName();
                return statModuleName.compareTo(module) == 0 && statMethodName.compareTo(method) == 0;
            }
        });

        ModuleMetricsVisitor visitor = null;
        while (iterator.hasNext()) {
            visitor = (ModuleMetricsVisitor) iterator.next();
            break;
        }

        if (visitor != null) {
            return visitor;
        } else {
            visitor = new ModuleMetricsVisitor(module, method);
            addModuleMetricsVisitor(visitor);
            return visitor;
        }
    }

    @Override
    public void run() {
        //利用ManagementFactory获取jvm,os,线程，死锁等一系列信息
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            //等待初始化完成
            latch.await();

            //在RMIServer上 创建本地主机上的“远程对象注册表”
            LocateRegistry.createRegistry(RpcSystemConfig.MODULE_METRICS_JMX_PORT);

            MsgRevExecutor msgRevExecutor = MsgRevExecutor.getInstance();
            String ipAddress = StringUtils.isNotEmpty(
                    msgRevExecutor.getServerAddress()) ?
                    StringUtils.substringBeforeLast(
                            msgRevExecutor.getServerAddress(), RpcSystemConfig.DELIMITER) : "localhost";
            moduleMetricsJmxUrl = "service:jmx:rmi:///jndi/rmi://" + ipAddress + ":" + RpcSystemConfig.MODULE_METRICS_JMX_PORT + "/rpcServer";
            JMXServiceURL url = new JMXServiceURL(moduleMetricsJmxUrl);
            JMXConnectorServer jmxConnectorServer =
                    JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
            //标准MBean名称必需是在要监控的类名后面加上“MBean”, 且要监控的类和MBean接口必需在同一包下
            ObjectName name = new ObjectName(RpcSystemConfig.MBEAN_NAME);

            mbs.registerMBean(ModuleMetricsHandler.this, name);
            mbs.addNotificationListener(name, listener, null, null);
            jmxConnectorServer.start();

            semaphoreWrapper.release();

            LOGGER.info("rpc JMX server is execute success! jmx-url: {}", moduleMetricsJmxUrl);
        } catch (Exception e) {
            LOGGER.error("exception {}", e);
        }
    }


    public void stop() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name = new ObjectName(RpcSystemConfig.MBEAN_NAME);
            mbs.unregisterMBean(name);
            //TODO 线程池未正确关闭
            ThreadPoolExecutor threadPoolExecutor = ExecutorManager.getJMXThreadPoolExecutor(METRICS_VISITOR_LIST_SIZE);
            threadPoolExecutor.shutdown();
            while (!threadPoolExecutor.isTerminated()) {
                LOGGER.error("jmx threadPoolExecutor is not terminated");
            }
        } catch (Exception e) {
            LOGGER.error("stop exception {}", e);
        }
    }

    public MBeanServerConnection connect() {
        try {
            if (!semaphoreWrapper.isRelease()) {
                semaphoreWrapper.acquire();
            }
            JMXServiceURL url = new JMXServiceURL(moduleMetricsJmxUrl);
            JMXConnector jmxConnector = JMXConnectorFactory.connect(url, null);
            connection = jmxConnector.getMBeanServerConnection();
        } catch (Exception e) {
            LOGGER.error("connect exception {}", e);
        }
        return connection;
    }

    public MBeanServerConnection getConnection() {
        return connection;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
}

