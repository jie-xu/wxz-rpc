package com.github.wxz.rpc.netty.core.invoke;

import com.github.wxz.rpc.netty.model.MsgRequest;
import com.github.wxz.rpc.netty.model.MsgResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -13:16
 */
public class MsgRevInitTaskAdapter extends AbstractMsgRevInitTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgRevInitTaskAdapter.class);

    public MsgRevInitTaskAdapter(MsgRequest msgRequest, MsgResponse msgResponse, Map<String, Object> handlerMap) {
        super(msgRequest, msgResponse, handlerMap);
    }

    @Override
    protected void injectInvoke() {
        LOGGER.info("injectFailInvoke............");
    }

    @Override
    protected void injectSuccessInvoke(long invokeTimeStamp) {
        LOGGER.info("injectSuccessInvoke............");

    }

    @Override
    protected void injectFailInvoke(Throwable error) {
        LOGGER.info("injectFailInvoke............");

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
