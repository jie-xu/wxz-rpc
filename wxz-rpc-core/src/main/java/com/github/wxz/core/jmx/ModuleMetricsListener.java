package com.github.wxz.core.jmx;


import com.github.wxz.core.rpc.event.AbstractInvokeEventBus;

import javax.management.AttributeChangeNotification;
import javax.management.JMException;
import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * @author xianzhi.wang
 * @date 2017/12/22 -21:11
 */
public class ModuleMetricsListener implements NotificationListener {
    @Override
    public void handleNotification(Notification notification, Object handback) {
        if (!(notification instanceof AttributeChangeNotification)) {
            return;
        }

        AttributeChangeNotification acn = (AttributeChangeNotification) notification;
        AbstractInvokeEventBus.ModuleEvent event = Enum.valueOf(AbstractInvokeEventBus.ModuleEvent.class, acn.getAttributeType());
        ModuleMetricsVisitor visitor = ModuleMetricsHandler.getInstance().visit(acn.getMessage(), acn.getAttributeName());

        switch (event) {
            case INVOKE_EVENT:
                visitor.setInvokeCount(((Long) acn.getNewValue()).longValue());
                break;
            case INVOKE_SUCCESS_EVENT:
                visitor.setInvokeSuccessCount(((Long) acn.getNewValue()).longValue());
                break;
            case INVOKE_FAIL_EVENT:
                visitor.setInvokeFailCount(((Long) acn.getNewValue()).longValue());
                break;
            case INVOKE_FILTER_EVENT:
                visitor.setInvokeFilterCount(((Long) acn.getNewValue()).longValue());
                break;
            case INVOKE_TIMESTAMP_EVENT:
                visitor.setInvokeTimeStamp(((Long) acn.getNewValue()).longValue());
                visitor.getHistogram().record(((Long) acn.getNewValue()).longValue());
                break;
            case INVOKE_MAX_TIMESTAMP_EVENT:
                if ((Long) acn.getNewValue() > (Long) acn.getOldValue()) {
                    visitor.setInvokeMaxTimeStamp(((Long) acn.getNewValue()).longValue());
                }
                break;
            case INVOKE_MIN_TIMESTAMP_EVENT:
                if ((Long) acn.getNewValue() < (Long) acn.getOldValue()) {
                    visitor.setInvokeMinTimeStamp(((Long) acn.getNewValue()).longValue());
                }
                break;
            case INVOKE_FAIL_STACKTRACE_EVENT:
                try {
                    visitor.setLastStackTrace((Exception) acn.getNewValue());
                    visitor.buildErrorCompositeData((Exception) acn.getNewValue());
                } catch (JMException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}

