package com.github.wxz.core.rpc.core.invoke;

import com.github.wxz.core.rpc.model.MsgRequest;
import com.github.wxz.core.rpc.model.MsgResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/22 -17:53
 */
public class MsgRevInitializeTask extends AbstractMsgRevInitTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgRevInitializeTask.class);

    public MsgRevInitializeTask(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        super(msgRequest, msgResponse, handlerMap);
    }

    @Override
    protected void injectInvoke() {
        LOGGER.info("injectFailInvoke............");
    }

    @Override
    protected void injectSuccessInvoke(long invokeTimeStamp) {
        LOGGER.info("injectSuccessInvoke............{}", invokeTimeStamp);
    }

    @Override
    protected void injectFailInvoke(Throwable error) {
        LOGGER.info("injectFailInvoke............{}", error);
    }

    @Override
    protected void injectFilterInvoke() {
        LOGGER.info("injectFilterInvoke............");
    }

    @Override
    protected void acquire() {
        LOGGER.info("acquire............");
    }

    @Override
    protected void release() {
        LOGGER.info("release............");
    }
}
