package com.github.wxz.rpc.core.invoke;

import com.github.wxz.rpc.model.MsgRequest;
import com.github.wxz.rpc.model.MsgResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 不开启监控
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
        LOGGER.info("injectSuccessInvoke............ {}", invokeTimeStamp);
    }

    @Override
    protected void injectFailInvoke(Throwable error) {
        LOGGER.info("injectFailInvoke............ {}", error);
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
