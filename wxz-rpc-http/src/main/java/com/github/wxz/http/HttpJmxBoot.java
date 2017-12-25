package com.github.wxz.http;

import com.github.wxz.core.config.RpcSystemConfig;
import com.github.wxz.core.jmx.ModuleMetricsHandler;
import com.github.wxz.core.jmx.hash.HashModuleMetricsVisitor;
import com.github.wxz.core.rpc.core.recv.MsgRevExecutor;
import com.github.wxz.core.rpc.parallel.ExecutorManager;

/**
 * @author xianzhi.wang
 * @date 2017/12/25 -12:44
 */
public class HttpJmxBoot implements Runnable{

    @Override
    public void run() {

        //开启JMS
        if (RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT) {
            HashModuleMetricsVisitor visitor = HashModuleMetricsVisitor.getInstance();

            //通过CountDownLatch使JMX初始化完成后才开始下一步JMX
            visitor.signal();
            ModuleMetricsHandler moduleMetricsHandler = ModuleMetricsHandler.getInstance();

            //begin..
            ExecutorManager.execute(
                    ExecutorManager.getJMXThreadPoolExecutor(5),
                    moduleMetricsHandler);
        }
    }
}
