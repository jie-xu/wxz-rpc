package com.github.wxz.rpc.jmx;

import com.github.wxz.rpc.config.RpcSystemConfig;
import com.github.wxz.rpc.netty.core.recv.MsgRevExecutor;
import com.github.wxz.rpc.parallel.AbstractDaemonThread;
import com.github.wxz.rpc.parallel.semaphore.SemaphoreWrapper;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;


/**
 * @author xianzhi.wang
 * @date 2017/12/22 -21:11
 */
public class ModuleMetricsHandler extends AbstractModuleMetricsHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleMetricsHandler.class);
    public final static String MBEAN_NAME = "com.github.wxz.rpc:type=ModuleMetricsHandler";
    public final static int MODULE_METRICS_JMX_PORT = 1098;
    private static final ModuleMetricsHandler INSTANCE = new ModuleMetricsHandler();
    private String moduleMetricsJmxUrl = "";
    private Semaphore semaphore = new Semaphore(0);
    private SemaphoreWrapper semaphoreWrapper = new SemaphoreWrapper(semaphore);
    private MBeanServerConnection connection;

    private CountDownLatch latch = new CountDownLatch(1);

    private ModuleMetricsListener listener = new ModuleMetricsListener();

    private ModuleMetricsHandler() {
        super();
    }

    public static ModuleMetricsHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public List<ModuleMetricsVisitor> getModuleMetricsVisitor() {
        return super.getModuleMetricsVisitor();
    }

    @Override
    protected ModuleMetricsVisitor visitCriticalSection(String moduleName, String methodName) {
        final String method = methodName.trim();
        final String module = moduleName.trim();

        //FIXME: 2017/10/13
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

    public void start() {
        new AbstractDaemonThread() {
            @Override
            public String getDaemonThreadName() {
                return ModuleMetricsHandler.class.getSimpleName();
            }

            @Override
            public void run() {
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                try {
                    latch.await();
                    LocateRegistry.createRegistry(MODULE_METRICS_JMX_PORT);
                    MsgRevExecutor ref = MsgRevExecutor.getInstance();
                    String ipAddress = StringUtils.isNotEmpty(
                            ref.getServerAddress()) ?
                            StringUtils.substringBeforeLast(
                                    ref.getServerAddress(), RpcSystemConfig.DELIMITER) : "localhost";
                    moduleMetricsJmxUrl = "service:jmx:rmi:///jndi/rmi://" + ipAddress + ":" + MODULE_METRICS_JMX_PORT + "/rpcServer";
                    JMXServiceURL url = new JMXServiceURL(moduleMetricsJmxUrl);
                    JMXConnectorServer cs =
                            JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);

                    ObjectName name = new ObjectName(MBEAN_NAME);

                    mbs.registerMBean(ModuleMetricsHandler.this, name);
                    mbs.addNotificationListener(name, listener, null, null);
                    cs.start();

                    semaphoreWrapper.release();

                    LOGGER.info("rpc JMX server is execute success! jmx-url: {}", moduleMetricsJmxUrl);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MBeanRegistrationException e) {
                    e.printStackTrace();
                } catch (InstanceAlreadyExistsException e) {
                    e.printStackTrace();
                } catch (NotCompliantMBeanException e) {
                    e.printStackTrace();
                } catch (MalformedObjectNameException e) {
                    e.printStackTrace();
                } catch (InstanceNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void stop() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name = new ObjectName(MBEAN_NAME);
            mbs.unregisterMBean(name);
            ExecutorService executor = getExecutor();
            executor.shutdown();
            while (!executor.isTerminated()) {

            }
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        }
    }

    public MBeanServerConnection connect() {
        try {
            if (!semaphoreWrapper.isRelease()) {
                semaphoreWrapper.acquire();
            }

            JMXServiceURL url = new JMXServiceURL(moduleMetricsJmxUrl);
            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
            connection = jmxc.getMBeanServerConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

