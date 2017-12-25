package com.github.wxz.rpc.spring;

import com.github.wxz.rpc.event.ServerStartEvent;
import com.github.wxz.rpc.filter.Filter;
import com.github.wxz.rpc.filter.ServiceFilterBinder;
import com.github.wxz.rpc.core.recv.MsgRevExecutor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 生产者
 *
 * @author xianzhi.wang
 * @date 2017/12/19 -15:51
 */
public class RpcService implements ApplicationContextAware, ApplicationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgRevExecutor.class);

    private String interfaceName;
    private String ref;
    private String filter;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        //事件源：ApplicationContext.publishEvent()方法：用于主动触发容器事件。
        //事件：ApplicationEvent类,容器事件，必须由ApplicationContext发布。
        applicationContext.publishEvent(new ServerStartEvent(new Object()));
    }


    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //解决加载两次的问题
        if (!(applicationEvent instanceof ContextRefreshedEvent)) {
            // 事件监听器：ApplicationListener接口，可由容器中任何监听器Bean担任。
            // onApplicationEvent(ApplicationEvent event)：每当容器内发生任何事件时，此方法都被触发
            ServiceFilterBinder binder = new ServiceFilterBinder();
            if (StringUtils.isBlank(filter) || !(applicationContext.getBean(filter) instanceof Filter)) {
                binder.setObject(applicationContext.getBean(ref));
            } else {
                binder.setObject(applicationContext.getBean(ref));
                binder.setFilter((Filter) applicationContext.getBean(filter));
            }
            MsgRevExecutor msgRevExecutor = MsgRevExecutor.getInstance();
            msgRevExecutor.getHandlerMap().putIfAbsent(interfaceName, binder);
            LOGGER.info("put interfaceName {}   into msgRevExecutor handlerMap ", interfaceName);
        } else {
            LOGGER.info("ContextRefreshedEvent ignore....");
        }
    }


    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
