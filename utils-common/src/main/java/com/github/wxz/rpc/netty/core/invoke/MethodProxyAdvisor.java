package com.github.wxz.rpc.netty.core.invoke;

import com.github.wxz.rpc.filter.Filter;
import com.github.wxz.rpc.filter.ServiceFilterBinder;
import com.github.wxz.rpc.model.MsgRequest;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -18:10
 */
public class MethodProxyAdvisor implements MethodInterceptor {
    private Map<String, Object> handlerMap;
    private boolean notNull = true;

    public MethodProxyAdvisor(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object[] arguments = methodInvocation.getArguments();

        if (arguments == null || arguments.length == 0) {
            return null;
        }
        MsgRequest msgRequest = (MsgRequest) arguments[0];

        String className = msgRequest.getClassName();
        Object serviceBean = handlerMap.get(className);
        String methodName = msgRequest.getMethodName();
        //获取参数
        Object[] parameters = msgRequest.getParametersVal();

        //filter是否存在
        boolean existFilter = ServiceFilterBinder.class.isAssignableFrom(serviceBean.getClass());
        ((MethodInvoker) methodInvocation.getThis()).setServiceBean(existFilter ? ((ServiceFilterBinder) serviceBean).getObject() : serviceBean);

        if (existFilter) {
            ServiceFilterBinder processors = (ServiceFilterBinder) serviceBean;
            if (processors.getFilter() != null) {
                Filter filter = processors.getFilter();
                Object[] args = ArrayUtils.nullToEmpty(parameters);
                Class<?>[] parameterTypes = ClassUtils.toClass(args);
                Method method = MethodUtils.getMatchingAccessibleMethod(processors.getObject().getClass(), methodName, parameterTypes);
                //调用拦截器
                if (filter.before(method, processors.getObject(), parameters)) {
                    //调用
                    Object result = methodInvocation.proceed();
                    filter.after(method, processors.getObject(), parameters);
                    setNotNull(result != null);
                    return result;
                } else {
                    return null;
                }
            }
        }

        Object result = methodInvocation.proceed();
        setNotNull(result != null);
        return result;

    }
}
