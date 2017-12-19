
package com.github.wxz.rpc.netty.exception;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -18:20
 */
public class CreateProxyException extends RuntimeException {
    public CreateProxyException() {
        super();
    }

    public CreateProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateProxyException(String message) {
        super(message);
    }

    public CreateProxyException(Throwable cause) {
        super(cause);
    }
}

