//package com.github.wxz.rpc.netty.jmx;
//
//
//import com.github.wxz.rpc.config.RpcSystemConfig;
//
//import javax.management.AttributeChangeNotification;
//import javax.management.MBeanNotificationInfo;
//import javax.management.NotificationBroadcasterSupport;
//import java.lang.management.ManagementFactory;
//import java.text.SimpleDateFormat;
//import java.other.Date;
//import java.other.List;
//import java.other.Queue;
//import java.other.concurrent.*;
//import java.other.concurrent.atomic.AtomicBoolean;
//import java.other.concurrent.locks.LockSupport;
//
///**
// * @author xianzhi.wang
// * @date 2017/12/20 -21:11
// */
//public abstract class AbstractModuleMetricsHandler extends NotificationBroadcasterSupport implements ModuleMetricsVisitorMXBean {
//    private static final int METRICS_VISITOR_LIST_SIZE = HashModuleMetricsVisitor.getInstance().getHashModuleMetricsVisitorListSize();
//    protected static String startTime;
//    private final AtomicBoolean locked = new AtomicBoolean(false);
//    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<Thread>();
//    protected List<ModuleMetricsVisitor> visitorList = new CopyOnWriteArrayList<ModuleMetricsVisitor>();
//    private MetricsTask[] tasks = new MetricsTask[METRICS_VISITOR_LIST_SIZE];
//    private boolean aggregationTaskFlag = false;
//    private ExecutorService executor = Executors.newFixedThreadPool(METRICS_VISITOR_LIST_SIZE);
//
//    public AbstractModuleMetricsHandler() {
//
//    }
//
//    public final static String getStartTime() {
//        if (startTime == null) {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            startTime = format.format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
//        }
//        return startTime;
//    }
//
//    public ModuleMetricsVisitor visit(String moduleName, String methodName) {
//        try {
//            enter();
//            return visitCriticalSection(moduleName, methodName);
//        } finally {
//            exit();
//        }
//    }
//
//    @Override
//    public List<ModuleMetricsVisitor> getModuleMetricsVisitor() {
//        if (RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_SUPPORT) {
//            CountDownLatch latch = new CountDownLatch(1);
//            MetricsAggregationTask aggregationTask = new MetricsAggregationTask(aggregationTaskFlag, tasks, visitorList, latch);
//            CyclicBarrier barrier = new CyclicBarrier(METRICS_VISITOR_LIST_SIZE, aggregationTask);
//            for (int i = 0; i < METRICS_VISITOR_LIST_SIZE; i++) {
//                tasks[i] = new MetricsTask(barrier, HashModuleMetricsVisitor.getInstance().getHashVisitorList().get(i));
//                executor.execute(tasks[i]);
//            }
//
//            try {
//                visitorList.clear();
//                latch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return visitorList;
//    }
//
//    @Override
//    public void addModuleMetricsVisitor(ModuleMetricsVisitor visitor) {
//        visitorList.add(visitor);
//    }
//
//    @Override
//    public MBeanNotificationInfo[] getNotificationInfo() {
//        String[] types = new String[]{
//                AttributeChangeNotification.ATTRIBUTE_CHANGE
//        };
//        String name = AttributeChangeNotification.class.getName();
//        String description = "the event send from NettyRPC server!";
//        MBeanNotificationInfo info =
//                new MBeanNotificationInfo(types, name, description);
//        return new MBeanNotificationInfo[]{info};
//    }
//
//    protected void enter() {
//        Thread current = Thread.currentThread();
//        waiters.add(current);
//
//        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
//            LockSupport.park(ModuleMetricsVisitor.class);
//        }
//
//        waiters.remove();
//
//    }
//
//    protected void exit() {
//        locked.set(false);
//        LockSupport.unpark(waiters.peek());
//    }
//
//    protected abstract ModuleMetricsVisitor visitCriticalSection(String moduleName, String methodName);
//
//    public ExecutorService getExecutor() {
//        return executor;
//    }
//
//    public void setExecutor(ExecutorService executor) {
//        this.executor = executor;
//    }
//}
//
