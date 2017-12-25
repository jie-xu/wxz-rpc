package com.github.wxz.core.jmx;

import com.alibaba.druid.util.Histogram;
import com.alibaba.fastjson.JSON;
import com.github.wxz.core.config.RpcSystemConfig;
import com.github.wxz.core.utils.DateUtils;

import javax.management.JMException;
import javax.management.openmbean.*;
import java.beans.ConstructorProperties;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * ModuleMetricsVisitor
 *
 * @author xianzhi.wang
 * @date 2017/12/22 -21:11
 */
public class ModuleMetricsVisitor {
    public static final long DEFAULT_INVOKE_MIN_TIMESTAMP = 3600 * 1000L;
    /**
     * 名称
     */
    private static final transient String[] THROWABLE_NAMES = {"message", "class", "stackTrace"};
    /**
     * 描述信息
     */
    private static final transient String[] THROWABLE_DESCRIPTIONS = {"message", "class", "stackTrace"};
    private static final transient OpenType<?>[] THROWABLE_TYPES = new OpenType<?>[]{SimpleType.STRING, SimpleType.STRING, SimpleType.STRING};
    /**
     * 复合类型
     */
    private static transient CompositeType THROWABLE_COMPOSITE_TYPE = null;
    private final transient AtomicLongFieldUpdater<ModuleMetricsVisitor> invokeCountUpdater = AtomicLongFieldUpdater.newUpdater(ModuleMetricsVisitor.class, "invokeCount");
    private final transient AtomicLongFieldUpdater<ModuleMetricsVisitor> invokeSuccessCountUpdater = AtomicLongFieldUpdater.newUpdater(ModuleMetricsVisitor.class, "invokeSuccessCount");
    private final transient AtomicLongFieldUpdater<ModuleMetricsVisitor> invokeFailCountUpdater = AtomicLongFieldUpdater.newUpdater(ModuleMetricsVisitor.class, "invokeFailCount");
    private final transient AtomicLongFieldUpdater<ModuleMetricsVisitor> invokeFilterCountUpdater = AtomicLongFieldUpdater.newUpdater(ModuleMetricsVisitor.class, "invokeFilterCount");
    /**
     * class 名称
     */
    private transient String moduleName;
    /**
     * 方法名称
     */
    private transient String methodName;
    private volatile long invokeCount = 0L;
    private volatile long invokeSuccessCount = 0L;
    private volatile long invokeFailCount = 0L;
    private volatile long invokeFilterCount = 0L;
    private long invokeTimeStamp = 0L;
    private long invokeMinTimeStamp = DEFAULT_INVOKE_MIN_TIMESTAMP;
    private long invokeMaxTimeStamp = 0L;
    private transient long[] invokeHistogram;
    private transient Exception lastStackTrace;
    private transient String lastStackTraceDetail;
    private long lastErrorTime;
    private transient int hashKey = 0;
    private transient Histogram histogram = new Histogram(TimeUnit.MILLISECONDS, new long[]{1, 10, 100, 1000, 10 * 1000, 100 * 1000, 1000 * 1000});

    @ConstructorProperties({"moduleName", "methodName"})
    public ModuleMetricsVisitor(String moduleName, String methodName) {
        this.moduleName = moduleName;
        this.methodName = methodName;
        clear();
    }

    /**
     * 初始化
     */
    public void clear() {
        lastStackTraceDetail = "";
        invokeTimeStamp = 0L;
        invokeMinTimeStamp = DEFAULT_INVOKE_MIN_TIMESTAMP;
        invokeMaxTimeStamp = 0L;
        lastErrorTime = 0L;
        lastStackTrace = null;
        invokeCountUpdater.set(this, 0);
        invokeSuccessCountUpdater.set(this, 0);
        invokeFailCountUpdater.set(this, 0);
        invokeFilterCountUpdater.set(this, 0);
        histogram.reset();
    }

    public void reset() {
        moduleName = "";
        methodName = "";
        clear();
    }

    public long getErrorLastTimeLongVal() {
        return lastErrorTime;
    }

    public void setErrorLastTimeLongVal(long lastErrorTime) {
        this.lastErrorTime = lastErrorTime;
    }

    public String getLastErrorTime() {
        if (lastErrorTime <= 0) {
            return null;
        }
        return DateUtils.localDateTimeToString(LocalDateTime.now());
    }

    public String getLastStackTrace() {
        if (lastStackTrace == null) {
            return null;
        }

        StringWriter buf = new StringWriter();
        lastStackTrace.printStackTrace(new PrintWriter(buf));
        return buf.toString();
    }

    public void setLastStackTrace(Exception lastStackTrace) {
        this.lastStackTrace = lastStackTrace;
        this.lastStackTraceDetail = getLastStackTrace();
        this.lastErrorTime = System.currentTimeMillis();
    }

    public String getStackTrace(Throwable ex) {
        StringWriter buf = new StringWriter();
        ex.printStackTrace(new PrintWriter(buf));

        return buf.toString();
    }

    public String getLastStackTraceDetail() {
        return lastStackTraceDetail;
    }

    public void setLastStackTraceDetail(String lastStackTraceDetail) {
        this.lastStackTraceDetail = lastStackTraceDetail;
    }

    public CompositeType getThrowableCompositeType() throws JMException {
        if (THROWABLE_COMPOSITE_TYPE == null) {
            THROWABLE_COMPOSITE_TYPE = new CompositeType("Throwable",
                    "Throwable",
                    THROWABLE_NAMES,
                    THROWABLE_DESCRIPTIONS,
                    THROWABLE_TYPES);
        }

        return THROWABLE_COMPOSITE_TYPE;
    }

    public CompositeData buildErrorCompositeData(Throwable error) throws JMException {
        if (error == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(512);
        map.put("class", error.getClass().getName());
        map.put("message", error.getMessage());
        map.put("stackTrace", getStackTrace(error));
        return new CompositeDataSupport(getThrowableCompositeType(), map);
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getInvokeCount() {
        return this.invokeCountUpdater.get(this);
    }

    public void setInvokeCount(long invokeCount) {
        this.invokeCountUpdater.set(this, invokeCount);
    }

    public long incrementInvokeCount() {
        return this.invokeCountUpdater.incrementAndGet(this);
    }

    public long getInvokeSuccessCount() {
        return this.invokeSuccessCountUpdater.get(this);
    }

    public void setInvokeSuccessCount(long invokeSuccessCount) {
        this.invokeSuccessCountUpdater.set(this, invokeSuccessCount);
    }

    public long incrementInvokeSuccessCount() {
        return this.invokeSuccessCountUpdater.incrementAndGet(this);
    }

    public long getInvokeFailCount() {
        return this.invokeFailCountUpdater.get(this);
    }

    public void setInvokeFailCount(long invokeFailCount) {
        this.invokeFailCountUpdater.set(this, invokeFailCount);
    }

    public long incrementInvokeFailCount() {
        return this.invokeFailCountUpdater.incrementAndGet(this);
    }

    public long getInvokeFilterCount() {
        return this.invokeFilterCountUpdater.get(this);
    }

    public void setInvokeFilterCount(long invokeFilterCount) {
        this.invokeFilterCountUpdater.set(this, invokeFilterCount);
    }

    public long incrementInvokeFilterCount() {
        return this.invokeFilterCountUpdater.incrementAndGet(this);
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public void setHistogram(Histogram histogram) {
        this.histogram = histogram;
    }

    public long[] getInvokeHistogram() {
        return RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_SUPPORT ? invokeHistogram : histogram.toArray();
    }

    public void setInvokeHistogram(long[] invokeHistogram) {
        this.invokeHistogram = invokeHistogram;
    }

    public long getInvokeTimeStamp() {
        return invokeTimeStamp;
    }

    public void setInvokeTimeStamp(long invokeTimeStamp) {
        this.invokeTimeStamp = invokeTimeStamp;
    }

    public long getInvokeMinTimeStamp() {
        return invokeMinTimeStamp;
    }

    public void setInvokeMinTimeStamp(long invokeMinTimeStamp) {
        this.invokeMinTimeStamp = invokeMinTimeStamp;
    }

    public long getInvokeMaxTimeStamp() {
        return invokeMaxTimeStamp;
    }

    public void setInvokeMaxTimeStamp(long invokeMaxTimeStamp) {
        this.invokeMaxTimeStamp = invokeMaxTimeStamp;
    }

    public int getHashKey() {
        return hashKey;
    }

    public void setHashKey(int hashKey) {
        this.hashKey = hashKey;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((moduleName == null) ? 0 : moduleName.hashCode());
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return moduleName.equals(((ModuleMetricsVisitor) obj).moduleName) && methodName.equals(((ModuleMetricsVisitor) obj).methodName);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

