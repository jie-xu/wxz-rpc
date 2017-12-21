//package com.github.wxz.rpc.netty.jmx;
//
//import com.github.wxz.rpc.config.RpcSystemConfig;
//import com.github.wxz.rpc.netty.core.recv.MsgRevExecutor;
//import org.apache.commons.collections.Predicate;
//import org.apache.commons.collections.iterators.FilterIterator;
//import org.apache.commons.lang3.StringUtils;
//
//import javax.management.*;
//import javax.management.remote.*;
//import java.io.IOException;
//import java.lang.management.ManagementFactory;
//import java.net.MalformedURLException;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.util.Iterator;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Semaphore;
//
//
///**
// * @author xianzhi.wang
// * @date 2017/12/20 -21:11
// */
//public class ModuleMetricsHandler extends AbstractModuleMetricsHandler {
//    public final static String MBEAN_NAME = "com.wxz.rpc:type=ModuleMetricsHandler";
//    public final static int MODULE_METRICS_JMX_PORT = 1098;
//    private static final ModuleMetricsHandler INSTANCE = new ModuleMetricsHandler();
//    private String moduleMetricsJmxUrl = "";
//    private Semaphore semaphore = new Semaphore(0);
//    private SemaphoreWrapper semaphoreWrapper = new SemaphoreWrapper(semaphore);
//    private MBeanServerConnection connection;
//    private CountDownLatch latch = new CountDownLatch(1);
//    private ModuleMetricsListener listener = new ModuleMetricsListener();
//
//    private ModuleMetricsHandler() {
//        super();
//    }
//
//    public static ModuleMetricsHandler getInstance() {
//        return INSTANCE;
//    }
//
//    @Override
//    public List<ModuleMetricsVisitor> getModuleMetricsVisitor() {
//        return super.getModuleMetricsVisitor();
//    }
//
//    @Override
//    protected ModuleMetricsVisitor visitCriticalSection(String moduleName, String methodName) {
//        final String method = methodName.trim();
//        final String module = moduleName.trim();
//
//        //FIXME: 2017/10/13 by tangjie
//        //JMX度量临界区要注意线程间的并发竞争,否则会统计数据失真
//        Iterator iterator = new FilterIterator(visitorList.iterator(), new Predicate() {
//            @Override
//            public boolean evaluate(Object object) {
//                String statModuleName = ((ModuleMetricsVisitor) object).getModuleName();
//                String statMethodName = ((ModuleMetricsVisitor) object).getMethodName();
//                return statModuleName.compareTo(module) == 0 && statMethodName.compareTo(method) == 0;
//            }
//        });
//
//        ModuleMetricsVisitor visitor = null;
//        while (iterator.hasNext()) {
//            visitor = (ModuleMetricsVisitor) iterator.next();
//            break;
//        }
//
//        if (visitor != null) {
//            return visitor;
//        } else {
//            visitor = new ModuleMetricsVisitor(module, method);
//            addModuleMetricsVisitor(visitor);
//            return visitor;
//        }
//    }
//
//    public void execute() {
//        new AbstractDaemonThread() {
//            @Override
//            public String getDeamonThreadName() {
//                return ModuleMetricsHandler.class.getSimpleName();
//            }
//
//            @Override
//            public void run() {
//                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//                try {
//                    latch.await();
//                    LocateRegistry.createRegistry(MODULE_METRICS_JMX_PORT);
//                    MsgRevExecutor ref = MsgRevExecutor.getInstance();
//                    String ipAddr = StringUtils.isNotEmpty(ref.getServerAddress()) ? StringUtils.substringBeforeLast(ref.getServerAddress(), RpcSystemConfig.DELIMITER) : "localhost";
//                    moduleMetricsJmxUrl = "service:jmx:rmi:///jndi/rmi://" + ipAddr + ":" + MODULE_METRICS_JMX_PORT + "/NettyRPCServer";
//                    JMXServiceURL url = new JMXServiceURL(moduleMetricsJmxUrl);
//                    JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
//
//                    ObjectName name = new ObjectName(MBEAN_NAME);
//
//                    mbs.registerMBean(ModuleMetricsHandler.this, name);
//                    mbs.addNotificationListener(name, listener, null, null);
//                    cs.execute();
//
//                    semaphoreWrapper.release();
//
//                    System.out.printf("NettyRPC JMX server is execute success!\njmx-url:[ %s ]\n\n", moduleMetricsJmxUrl);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (MBeanRegistrationException e) {
//                    e.printStackTrace();
//                } catch (InstanceAlreadyExistsException e) {
//                    e.printStackTrace();
//                } catch (NotCompliantMBeanException e) {
//                    e.printStackTrace();
//                } catch (MalformedObjectNameException e) {
//                    e.printStackTrace();
//                } catch (InstanceNotFoundException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.execute();
//    }
//
//    public void stop() {
//        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//        try {
//            ObjectName name = new ObjectName(MBEAN_NAME);
//            mbs.unregisterMBean(name);
//            ExecutorService executor = getExecutor();
//            executor.shutDown();
//            while (!executor.isTerminated()) {
//
//            }
//        } catch (MalformedObjectNameException e) {
//            e.printStackTrace();
//        } catch (InstanceNotFoundException e) {
//            e.printStackTrace();
//        } catch (MBeanRegistrationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public MBeanServerConnection connect() {
//        try {
//            if (!semaphoreWrapper.isRelease()) {
//                semaphoreWrapper.acquire();
//            }
//
//            JMXServiceURL url = new JMXServiceURL(moduleMetricsJmxUrl);
//            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
//            connection = jmxc.getMBeanServerConnection();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return connection;
//    }
//
//    public MBeanServerConnection getConnection() {
//        return connection;
//    }
//
//    public CountDownLatch getLatch() {
//        return latch;
//    }
//
//    public void setLatch(CountDownLatch latch) {
//        this.latch = latch;
//    }
//}
//
