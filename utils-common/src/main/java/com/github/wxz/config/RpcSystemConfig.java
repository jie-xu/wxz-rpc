package com.github.wxz.config;

import java.io.File;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:52
 */
public class RpcSystemConfig {

    public static final String FAVICON_ICO = "/favicon.ico";

    public static final String TEMPLATE_PATH = "templates";
    /**
     * class path
     */
    public static final String CLASS_PATH = new File(RpcSystemConfig.class.getResource("/").getPath()).getPath();

    public static final String SYSTEM_PROPERTY_THREAD_POOL_REJECTED_POLICY_ATTR = "rpc.parallel.rejected.policy";

    public static final String SYSTEM_PROPERTY_THREAD_POOL_QUEUE_NAME_ATTR = "rpc.parallel.queue";

    public static final long SYSTEM_PROPERTY_MESSAGE_CALLBACK_TIMEOUT = Long.getLong("rpc.default.msg.timeout", 300 * 1000L);

    public static final long SYSTEM_PROPERTY_ASYNC_MESSAGE_CALLBACK_TIMEOUT = Long.getLong("rpc.default.asyncmsg.timeout", 60 * 1000L);

    public static final int SYSTEM_PROPERTY_THREAD_POOL_THREAD_NUMS = Integer.getInteger("rpc.default.thread.nums", 16);

    public static final int SYSTEM_PROPERTY_THREAD_POOL_QUEUE_NUMS = Integer.getInteger("rpc.default.queue.nums", -1);

    public static final int SYSTEM_PROPERTY_CLIENT_RECONNECT_DELAY = Integer.parseInt(System.getProperty("rpc.default.client.reconnect.delay", "10"));

    public static final int SYSTEM_PROPERTY_PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());
    /**
     * num
     */
    public static final int SYSTEM_PROPERTY_JMX_METRICS_HASH_NUMS = Integer.getInteger("rpc.jmx.metrics.hash.nums", 8);
    /**
     * fair表示是否是公平的，即等待时间越久的越先获取许可
     */
    public static final int SYSTEM_PROPERTY_JMX_METRICS_LOCK_FAIR = Integer.getInteger("rpc.jmx.metrics.lock.fair", 0);

    public static final boolean SYSTEM_PROPERTY_JMX_METRICS_HASH_SUPPORT = RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_NUMS != 1;

    public static final String DELIMITER = ":";

    public static final int IP_ADDRESS_PORT_ARRAY_LENGTH = 2;

    public static final String RPC_COMPILER_SPI_ATTR = "com.wxz.rpc.compiler.AccessAdaptive";

    public static final String RPC_ABILITY_DETAIL_SPI_ATTR = "com.github.wxz.rpc.ability.AbilityDetail";

    public static final String FILTER_RESPONSE_MSG = "illegal request,rpc server refused to respond!";

    public static final String TIMEOUT_RESPONSE_MSG = "timeout request,rpc server request timeout!";

    public static final int SERIALIZE_POOL_MAX_TOTAL = 500;

    public static final int SERIALIZE_POOL_MIN_IDLE = 10;

    public static final int SERIALIZE_POOL_MAX_WAIT_MILLIS = 5000;

    public static final int SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS = 600000;
    /**
     * module_metrics_jmx_port  RMIService监听的端口
     */
    public final static int MODULE_METRICS_JMX_PORT = 1098;
    /**
     * MBEAN_NAME
     */
    public final static String MBEAN_NAME = "com.github.wxz.rpc:type=ModuleMetricsHandler";
    private static final int SYSTEM_PROPERTY_JMX_INVOKE_METRICS = Integer.getInteger("rpc.jmx.invoke.metrics", 1);
    /**
     * JMX_METRICS_SUPPORT
     */
    public static final boolean SYSTEM_PROPERTY_JMX_METRICS_SUPPORT = RpcSystemConfig.SYSTEM_PROPERTY_JMX_INVOKE_METRICS != 0;
    private static boolean monitorServerSupport = false;

    public static boolean isMonitorServerSupport() {
        return monitorServerSupport;
    }

    public static void setMonitorServerSupport(boolean jmxSupport) {
        monitorServerSupport = jmxSupport;
    }
}