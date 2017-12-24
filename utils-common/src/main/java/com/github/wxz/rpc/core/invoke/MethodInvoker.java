package com.github.wxz.rpc.core.invoke;

import com.github.wxz.rpc.model.MsgRequest;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * method invoke
 *
 * @author xianzhi.wang
 * @date 2017/12/21 -15:32
 */
public class MethodInvoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodInvoker.class);
    private StopWatch stopWatch = new StopWatch();
    private Object serviceBean;

    public Object getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(Object serviceBean) {
        this.serviceBean = serviceBean;
    }

    public Object invoke(MsgRequest msgRequest) {
        String methodName = msgRequest.getMethodName();
        Object[] parametersVal = msgRequest.getParametersVal();
        Object result = null;
        stopWatch.reset();
        stopWatch.start();
        try {
            result = MethodUtils.invokeMethod(serviceBean, methodName, parametersVal);
        } catch (NoSuchMethodException e) {
            LOGGER.error("NoSuchMethodException", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            LOGGER.error("InvocationTargetException", e);
        }
        stopWatch.stop();
        return result;

    }

    public long getInvokeTimeStamp() {
        return stopWatch.getTime();
    }
}
