package com.github.wxz.rpc.netty.core.invoke.hash;

import com.github.wxz.rpc.filter.ServiceFilterBinder;
import com.github.wxz.rpc.jmx.ModuleMetricsVisitor;
import com.github.wxz.rpc.jmx.hash.HashModuleMetricsVisitor;
import com.github.wxz.rpc.netty.core.invoke.AbstractMsgRevInitTask;
import com.github.wxz.rpc.model.MsgRequest;
import com.github.wxz.rpc.model.MsgResponse;
import com.github.wxz.rpc.utils.ReflectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.JMException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * HashMsgRevInitializeTask
 *
 * @author xianzhi.wang
 * @date 2017/12/22 -17:53
 */
public class HashMsgRevInitializeTask extends AbstractMsgRevInitTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashMsgRevInitializeTask.class);
    private static HashCriticalSection criticalSection = new HashCriticalSection();
    private int hashKey = 0;
    private AtomicReference<ModuleMetricsVisitor> visitor = new AtomicReference<>();


    public HashMsgRevInitializeTask(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        super(msgRequest, msgResponse, handlerMap);
        hashKey = HashCriticalSection.hash(msgRequest.getMessageId());

    }

    @Override
    protected void injectInvoke() {
        LOGGER.info("injectInvoke............");
        Class cls = handlerMap.get(msgRequest.getClassName()).getClass();
        boolean binder = ServiceFilterBinder.class.isAssignableFrom(cls);
        if (binder) {
            cls = ((ServiceFilterBinder) handlerMap.get(msgRequest.getClassName())).getObject().getClass();
        }

        ReflectionUtils utils = new ReflectionUtils();

        try {
            Method method = ReflectionUtils.getDeclaredMethod(
                    cls, msgRequest.getMethodName(),
                    msgRequest.getTypeParameters());
            utils.listMethod(method, false);
            String signatureMethod = utils.getProvider().toString().trim();

            int index = getHashVisitorListIndex(signatureMethod);
            List<ModuleMetricsVisitor> metricsVisitor =
                    HashModuleMetricsVisitor
                    .getInstance()
                    .getHashVisitorList()
                    .get(index);
            visitor.set(metricsVisitor.get(hashKey));
            incrementInvoke(visitor.get());
        } finally {
            utils.clearProvider();
        }
    }

    @Override
    protected void injectSuccessInvoke(long invokeTimeStamp) {
        LOGGER.info("injectSuccessInvoke............{}", invokeTimeStamp);
        incrementInvokeSuccess(visitor.get(), invokeTimeStamp);
    }

    @Override
    protected void injectFailInvoke(Throwable error) {
        LOGGER.info("injectFailInvoke............{}", error);
        incrementInvokeFail(visitor.get(), error);
    }

    @Override
    protected void injectFilterInvoke() {
        LOGGER.info("injectFilterInvoke............");
        incrementInvokeFilter(visitor.get());

    }

    @Override
    protected void acquire() {
        criticalSection.enter(hashKey);
        LOGGER.info("acquire............");

    }

    @Override
    protected void release() {
        criticalSection.exit(hashKey);
        LOGGER.info("release............");

    }

    private int getHashVisitorListIndex(String signatureMethod) {
        int index;
        int size = HashModuleMetricsVisitor.getInstance().getHashModuleMetricsVisitorListSize();
        breakFor:
        for (index = 0; index < size; index++) {
            Iterator iterator = new FilterIterator(
                    HashModuleMetricsVisitor.getInstance().getHashVisitorList().get(index).iterator(),
                    new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    String statModuleName = ((ModuleMetricsVisitor) object).getModuleName();
                    String statMethodName = ((ModuleMetricsVisitor) object).getMethodName();
                    return statModuleName.compareTo(msgRequest.getClassName()) == 0 && statMethodName.compareTo(signatureMethod) == 0;
                }
            });

            while (iterator.hasNext()) {
                break breakFor;
            }
        }
        return index;
    }

    /**
     * increment invoke
     * @param visitor
     */
    private void incrementInvoke(ModuleMetricsVisitor visitor) {
        visitor.setHashKey(hashKey);
        visitor.incrementInvokeCount();
    }

    /**
     * invoke fail increment
     *
     * @param visitor
     * @param error
     */
    private void incrementInvokeFail(ModuleMetricsVisitor visitor, Throwable error) {
        visitor.incrementInvokeFailCount();
        visitor.setLastStackTrace((Exception) error);
        try {
            visitor.buildErrorCompositeData(error);
        } catch (JMException e) {
            LOGGER.error("incrementInvokeFail buildErrorCompositeData {} ", e);
        }
    }

    /**
     * increment invoke success
     *
     * @param visitor
     * @param invokeTimeStamp
     */
    private void incrementInvokeSuccess(ModuleMetricsVisitor visitor, long invokeTimeStamp) {
        visitor.incrementInvokeSuccessCount();
        visitor.getHistogram().record(invokeTimeStamp);
        visitor.setInvokeTimeStamp(invokeTimeStamp);

        if (invokeTimeStamp < visitor.getInvokeTimeStamp()) {
            visitor.setInvokeMinTimeStamp(invokeTimeStamp);
        }
        if (invokeTimeStamp > visitor.getInvokeTimeStamp()) {
            visitor.setInvokeMaxTimeStamp(invokeTimeStamp);
        }
    }

    /**
     * increment invoke filter
     * @param visitor
     */
    private void incrementInvokeFilter(ModuleMetricsVisitor visitor) {
        visitor.incrementInvokeFilterCount();
    }
}
