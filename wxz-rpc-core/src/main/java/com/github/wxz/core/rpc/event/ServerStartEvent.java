package com.github.wxz.core.rpc.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:20
 */
public class ServerStartEvent extends ApplicationEvent {
    public ServerStartEvent(Object source) {
        super(source);
    }
}
