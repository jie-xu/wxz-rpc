package com.github.wxz.jmx;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author xianzhi.wang
 * @date 2017/12/22 -21:11
 */
public class MetricsAggregationTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsAggregationTask.class);
    private boolean flag = false;
    private MetricsTask[] tasks;
    private List<ModuleMetricsVisitor> visitors;
    private CountDownLatch latch;

    public MetricsAggregationTask(boolean flag, MetricsTask[] tasks, List<ModuleMetricsVisitor> visitors, CountDownLatch latch) {
        this.flag = flag;
        this.tasks = tasks;
        this.visitors = visitors;
        this.latch = latch;
    }

    @Override
    public void run() {
        if (flag) {
            try {
                for (MetricsTask task : tasks) {
                    LOGGER.info("result {}", task.getResult().get(0));
                    visitors.add(task.getResult().get(0));
                }
            } finally {
                latch.countDown();
            }
        } else {
            flag = true;
        }
    }
}

