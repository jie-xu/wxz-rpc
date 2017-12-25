package com.github.wxz.core.jmx;

import org.apache.commons.collections.iterators.UniqueFilterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/**
 * @author xianzhi.wang
 * @date 2017/12/22 -21:11
 */
public class MetricsTask implements Runnable {
    private final CyclicBarrier barrier;
    private List<ModuleMetricsVisitor> visitorList;
    private List<ModuleMetricsVisitor> result = new ArrayList<ModuleMetricsVisitor>();

    public MetricsTask(CyclicBarrier barrier, List<ModuleMetricsVisitor> visitorList) {
        this.barrier = barrier;
        this.visitorList = visitorList;
    }

    @Override
    public void run() {
        try {
            barrier.await();
            accumulate();
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void count(List<ModuleMetricsVisitor> list) {
        for (int i = 0; i < result.size(); i++) {
            long invokeCount = 0L;
            long invokeSuccessCount = 0L;
            long invokeFailCount = 0L;
            long invokeFilterCount = 0L;
            long invokeTimeStamp = 0L;
            long invokeMinTimeStamp = list.get(0).getInvokeMinTimeStamp();
            long invokeMaxTimeStamp = list.get(0).getInvokeMaxTimeStamp();
            int length = result.get(i).getHistogram().getRanges().length + 1;
            long[] invokeHistogram = new long[length];
            Arrays.fill(invokeHistogram, 0L);
            String lastStackTraceDetail = "";
            long lastErrorTime = list.get(0).getErrorLastTimeLongVal();

            ModuleMetrics metrics = new ModuleMetrics();
            metrics.setInvokeCount(invokeCount);
            metrics.setInvokeSuccessCount(invokeSuccessCount);
            metrics.setInvokeFailCount(invokeFailCount);
            metrics.setInvokeFilterCount(invokeFilterCount);
            metrics.setInvokeTimeStamp(invokeTimeStamp);
            metrics.setInvokeMinTimeStamp(invokeMinTimeStamp);
            metrics.setInvokeMaxTimeStamp(invokeMaxTimeStamp);
            metrics.setInvokeHistogram(invokeHistogram);
            metrics.setLastStackTraceDetail(lastStackTraceDetail);
            metrics.setLastErrorTime(lastErrorTime);

            merge(i, list, metrics);

            result.get(i).setInvokeCount(metrics.getInvokeCount());
            result.get(i).setInvokeSuccessCount(metrics.getInvokeSuccessCount());
            result.get(i).setInvokeFailCount(metrics.getInvokeFailCount());
            result.get(i).setInvokeFilterCount(metrics.getInvokeFilterCount());
            result.get(i).setInvokeTimeStamp(metrics.getInvokeTimeStamp());
            result.get(i).setInvokeMaxTimeStamp(metrics.getInvokeMaxTimeStamp());
            result.get(i).setInvokeMinTimeStamp(metrics.getInvokeMinTimeStamp());
            result.get(i).setInvokeHistogram(metrics.getInvokeHistogram());

            if (metrics.getLastErrorTime() > 0) {
                result.get(i).setErrorLastTimeLongVal(metrics.getLastErrorTime());
                result.get(i).setLastStackTraceDetail(metrics.getLastStackTraceDetail());
            }
        }
    }

    private void merge(int index, List<ModuleMetricsVisitor> list, ModuleMetrics metrics) {
        long invokeCount = metrics.getInvokeCount();
        long invokeSuccessCount = metrics.getInvokeSuccessCount();
        long invokeFailCount = metrics.getInvokeFailCount();
        long invokeFilterCount = metrics.getInvokeFilterCount();
        long invokeTimeStamp = metrics.getInvokeTimeStamp();
        long invokeMinTimeStamp = metrics.getInvokeMinTimeStamp();
        long invokeMaxTimeStamp = metrics.getInvokeMaxTimeStamp();
        long[] invokeHistogram = metrics.getInvokeHistogram();
        String lastStackTraceDetail = metrics.getLastStackTraceDetail();
        long lastErrorTime = metrics.getLastErrorTime();

        for (int i = 0; i < list.size(); i++) {
            boolean find = equals(result.get(index).getModuleName(), list.get(i).getModuleName(), result.get(index).getMethodName(), list.get(i).getMethodName());
            if (find) {
                invokeCount += list.get(i).getInvokeCount();
                invokeSuccessCount += list.get(i).getInvokeSuccessCount();
                invokeFailCount += list.get(i).getInvokeFailCount();
                invokeFilterCount += list.get(i).getInvokeFilterCount();
                long timeStamp = list.get(i).getInvokeTimeStamp();
                if (timeStamp > 0) {
                    invokeTimeStamp = timeStamp;
                }
                long minTimeStamp = list.get(i).getInvokeMinTimeStamp();
                long maxTimeStamp = list.get(i).getInvokeMaxTimeStamp();
                if (minTimeStamp < invokeMinTimeStamp) {
                    invokeMinTimeStamp = minTimeStamp;
                }
                if (maxTimeStamp > invokeMaxTimeStamp) {
                    invokeMaxTimeStamp = maxTimeStamp;
                }

                for (int j = 0; j < invokeHistogram.length; j++) {
                    invokeHistogram[j] += list.get(i).getHistogram().toArray()[j];
                }

                long fail = list.get(i).getInvokeFailCount();
                if (fail > 0) {
                    long lastTime = list.get(i).getErrorLastTimeLongVal();
                    if (lastTime > lastErrorTime) {
                        lastErrorTime = lastTime;
                        lastStackTraceDetail = list.get(i).getLastStackTraceDetail();
                    }
                }
            }
        }

        metrics.setInvokeCount(invokeCount);
        metrics.setInvokeSuccessCount(invokeSuccessCount);
        metrics.setInvokeFailCount(invokeFailCount);
        metrics.setInvokeFilterCount(invokeFilterCount);
        metrics.setInvokeTimeStamp(invokeTimeStamp);
        metrics.setInvokeMinTimeStamp(invokeMinTimeStamp);
        metrics.setInvokeMaxTimeStamp(invokeMaxTimeStamp);
        metrics.setInvokeHistogram(invokeHistogram);
        metrics.setLastStackTraceDetail(lastStackTraceDetail);
        metrics.setLastErrorTime(lastErrorTime);
    }

    private void accumulate() {
        List<ModuleMetricsVisitor> list = visitorList;

        Iterator iterator = new UniqueFilterIterator(list.iterator());
        while (iterator.hasNext()) {
            ModuleMetricsVisitor visitor = (ModuleMetricsVisitor) iterator.next();
            result.add(new ModuleMetricsVisitor(visitor.getModuleName(), visitor.getMethodName()));
        }

        count(list);
    }

    private boolean equals(String srcModuleName, String destModuleName, String srcMethodName, String destMethodName) {
        return srcModuleName.equals(destModuleName) && srcMethodName.equals(destMethodName);
    }

    public List<ModuleMetricsVisitor> getResult() {
        return result;
    }

    public void setResult(List<ModuleMetricsVisitor> result) {
        this.result = result;
    }

    private class ModuleMetrics {
        private long invokeCount;
        private long invokeSuccessCount;
        private long invokeFailCount;
        private long invokeFilterCount;
        private long invokeTimeStamp;
        private long invokeMinTimeStamp;
        private long invokeMaxTimeStamp;
        private long[] invokeHistogram;
        private String lastStackTraceDetail;
        private long lastErrorTime;

        public long getInvokeCount() {
            return invokeCount;
        }

        public void setInvokeCount(long invokeCount) {
            this.invokeCount = invokeCount;
        }

        public long getInvokeSuccessCount() {
            return invokeSuccessCount;
        }

        public void setInvokeSuccessCount(long invokeSuccessCount) {
            this.invokeSuccessCount = invokeSuccessCount;
        }

        public long getInvokeFailCount() {
            return invokeFailCount;
        }

        public void setInvokeFailCount(long invokeFailCount) {
            this.invokeFailCount = invokeFailCount;
        }

        public long getInvokeFilterCount() {
            return invokeFilterCount;
        }

        public void setInvokeFilterCount(long invokeFilterCount) {
            this.invokeFilterCount = invokeFilterCount;
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

        public long[] getInvokeHistogram() {
            return invokeHistogram;
        }

        public void setInvokeHistogram(long[] invokeHistogram) {
            this.invokeHistogram = invokeHistogram;
        }

        public String getLastStackTraceDetail() {
            return lastStackTraceDetail;
        }

        public void setLastStackTraceDetail(String lastStackTraceDetail) {
            this.lastStackTraceDetail = lastStackTraceDetail;
        }

        public long getLastErrorTime() {
            return lastErrorTime;
        }

        public void setLastErrorTime(long lastErrorTime) {
            this.lastErrorTime = lastErrorTime;
        }
    }
}

