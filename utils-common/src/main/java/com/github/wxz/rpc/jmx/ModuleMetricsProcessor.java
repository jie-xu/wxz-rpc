package com.github.wxz.rpc.jmx;

import com.github.wxz.rpc.config.RpcSystemConfig;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * ModuleMetricsProcessor
 *
 * @author xianzhi.wang
 * @date 2017/12/22 -21:11
 */
public class ModuleMetricsProcessor {
    private final static String TD_BEGIN = "<td>";
    private final static String TD_END = "</td>";
    private final static String TR_BEGIN = "<tr>";
    private final static String TR_END = "</tr>";
    private final static String BR = "</br>";
    private final static String TABLE_BEGIN = "<html><body><div class=\"table-container\"><table border=\"1\"><tr><th>模块名称</th><th>方法名称</th><th>调用次数</th><th>调用成功次数</th><th>调用失败次数</th><th>被过滤次数</th><th>方法耗时（毫秒）</th><th>方法最大耗时（毫秒）</th><th>方法最小耗时（毫秒）</th><th>方法耗时区间分布</th><th>最后一次失败时间</th><th>最后一次失败堆栈明细</th></tr>";
    private final static String TABLE_END = "</table></body></html>";
    private final static String SUB_TABLE_BEGIN = "<table border=\"1\">";
    private final static String SUB_TABLE_END = "</table>";
    private final static String JMX_METRICS_ATTR = "ModuleMetricsVisitor";

    private static volatile ModuleMetricsProcessor moduleMetricsProcessor = null;

    private MBeanServerConnection mBeanServerConnection;

    private ModuleMetricsProcessor() {
        init();
    }

    /**
     * instance
     * @return
     */
    public static ModuleMetricsProcessor getInstance() {
        if (moduleMetricsProcessor == null) {
            synchronized (ModuleMetricsProcessor.class) {
                if (moduleMetricsProcessor == null) {
                    moduleMetricsProcessor = new ModuleMetricsProcessor();
                }
            }
        }
        return moduleMetricsProcessor;
    }

    /**
     * 获取连接
     */
    private void init() {
        ModuleMetricsHandler handler = ModuleMetricsHandler.getInstance();
        mBeanServerConnection = handler.connect();

        while (true) {
            if (mBeanServerConnection != null) {
                break;
            } else {
                try {
                    TimeUnit.SECONDS.sleep(1L);
                    mBeanServerConnection = handler.connect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String buildHistogram(CompositeData data) {
        CompositeDataSupport histogram = (CompositeDataSupport) (data.get("histogram"));
        long[] ranges = (long[]) (histogram.get("ranges"));
        long[] invokeHistogram = (long[]) (data.get("invokeHistogram"));
        StringBuilder distribute = new StringBuilder();
        distribute.append(SUB_TABLE_BEGIN);
        int i = 0;
        for (; i < ranges.length; i++) {
            distribute.append(TR_BEGIN + TD_BEGIN + ((i == 0) ? i : ranges[i - 1]) + "~" + ranges[i] + "毫秒的笔数：" + invokeHistogram[i] + BR + TD_END + TR_END);
        }
        distribute.append(TR_BEGIN + TD_BEGIN + "大于等于" + ranges[i - 1] + "毫秒的笔数：" + invokeHistogram[i] + BR + TD_END + TR_END);
        distribute.append(SUB_TABLE_END);
        return distribute.toString();
    }

    public String buildModuleMetrics() {
        StringBuilder metrics = new StringBuilder();

        metrics.append(TABLE_BEGIN);
        ObjectName name = null;
        try {
            name = new ObjectName(RpcSystemConfig.MBEAN_NAME);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }

        try {
            Object obj = mBeanServerConnection.getAttribute(name, JMX_METRICS_ATTR);
            if (obj instanceof CompositeData[]) {
                for (CompositeData compositeData : (CompositeData[]) obj) {
                    CompositeData data = (CompositeData) compositeData;
                    String moduleName = (String) (data.get("moduleName"));
                    String methodName = (String) (data.get("methodName"));
                    long invokeCount = (Long) (data.get("invokeCount"));
                    long invokeSuccessCount = (Long) (data.get("invokeSuccessCount"));
                    long invokeFailCount = (Long) (data.get("invokeFailCount"));
                    long invokeFilterCount = (Long) (data.get("invokeFilterCount"));
                    long invokeTimeStamp = (Long) (data.get("invokeTimeStamp"));
                    long invokeMinTimeStamp = ((Long) (data.get("invokeMinTimeStamp"))).equals(Long.valueOf(ModuleMetricsVisitor.DEFAULT_INVOKE_MIN_TIMESTAMP)) ? Long.valueOf(0L) : (Long) (data.get("invokeMinTimeStamp"));
                    long invokeMaxTimeStamp = (Long) (data.get("invokeMaxTimeStamp"));
                    String lastStackTraceDetail = (String) (data.get("lastStackTraceDetail"));
                    String lastErrorTime = (String) (data.get("lastErrorTime"));
                    String distribute = buildHistogram(data);
                    metrics.append(TR_BEGIN);
                    metrics.append(TD_BEGIN + moduleName + TD_END);
                    metrics.append(TD_BEGIN + methodName + TD_END);
                    metrics.append(TD_BEGIN + invokeCount + TD_END);
                    metrics.append(TD_BEGIN + invokeSuccessCount + TD_END);
                    metrics.append(TD_BEGIN + invokeFailCount + TD_END);
                    metrics.append(TD_BEGIN + invokeFilterCount + TD_END);
                    metrics.append(TD_BEGIN + invokeTimeStamp + TD_END);
                    metrics.append(TD_BEGIN + invokeMinTimeStamp + TD_END);
                    metrics.append(TD_BEGIN + invokeMaxTimeStamp + TD_END);
                    metrics.append(TD_BEGIN + distribute + TD_END);
                    metrics.append(TD_BEGIN + (lastErrorTime != null ? lastErrorTime : "") + TD_END);
                    metrics.append(TD_BEGIN + lastStackTraceDetail + TD_END);
                    metrics.append(TR_END);
                }
            }
            metrics.append(TABLE_END);
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return metrics.toString();
    }
}

