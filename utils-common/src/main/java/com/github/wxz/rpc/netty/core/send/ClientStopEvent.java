package com.github.wxz.rpc.netty.core.send;

/**
 * Client Stop Event
 *
 * @author xianzhi.wang
 * @date 2017/12/21 -9:43
 */
public class ClientStopEvent {
    private final int message;

    public ClientStopEvent(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }
}
