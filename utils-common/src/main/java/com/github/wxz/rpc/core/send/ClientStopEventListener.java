package com.github.wxz.rpc.core.send;

import com.google.common.eventbus.Subscribe;

/**
 * @author xianzhi.wang
 * @date 2017/12/21 -9:43
 */
public class ClientStopEventListener {
    public int lastMessage = 0;

    /**
     * @Subscribe
     * @param event
     */
    @Subscribe
    public void listen(ClientStopEvent event) {
        lastMessage = event.getMessage();
        MsgSendExecutor.getInstance().stop();
    }

    public int getLastMessage() {
        return lastMessage;
    }
}
