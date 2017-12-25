package com.github.wxz.core.exception;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -18:20
 */
public class InvokeTimeoutException extends RuntimeException {
    public InvokeTimeoutException() {
        super();
    }

    public InvokeTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvokeTimeoutException(String message) {
        super(message);
    }

    public InvokeTimeoutException(Throwable cause) {
        super(cause);
    }
}

