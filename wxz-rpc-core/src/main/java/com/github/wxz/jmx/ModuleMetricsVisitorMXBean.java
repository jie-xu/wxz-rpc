package com.github.wxz.jmx;


import java.util.List;

/**
 * @author xianzhi.wang
 * @date 2017/12/22 -21:11
 */
public interface ModuleMetricsVisitorMXBean {
    /**
     * getModuleMetricsVisitor
     *
     * @return
     */
    List<ModuleMetricsVisitor> getModuleMetricsVisitor();

    /**
     * addModuleMetricsVisitor
     *
     * @param visitor
     */
    void addModuleMetricsVisitor(ModuleMetricsVisitor visitor);
}

