package com.github.wxz.rpc.netty.core.invoke.hash;

import com.github.wxz.rpc.netty.core.invoke.AbstractMsgRevInitTask;
import com.github.wxz.rpc.netty.model.MsgRequest;
import com.github.wxz.rpc.netty.model.MsgResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 *
 * @author xianzhi.wang
 * @date 2017/12/22 -17:53
 */
public class HashMsgRevInitializeTask extends AbstractMsgRevInitTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(HashMsgRevInitializeTask.class);

    public HashMsgRevInitializeTask(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        super(msgRequest, msgResponse, handlerMap);
    }

    @Override
    protected void injectInvoke() {
        LOGGER.info("injectFailInvoke............");

    }

    @Override
    protected void injectSuccessInvoke(long invokeTimeStamp) {
        LOGGER.info("injectSuccessInvoke............{}",invokeTimeStamp);

    }

    @Override
    protected void injectFailInvoke(Throwable error) {
        LOGGER.info("injectFailInvoke............{}",error);

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
