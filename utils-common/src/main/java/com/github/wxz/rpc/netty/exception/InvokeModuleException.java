
package com.github.wxz.rpc.netty.exception;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -18:20
 */
public class InvokeModuleException extends RuntimeException {
    public InvokeModuleException() {
        super();
    }

    public InvokeModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvokeModuleException(String message) {
        super(message);
    }

    public InvokeModuleException(Throwable cause) {
        super(cause);
    }
}

