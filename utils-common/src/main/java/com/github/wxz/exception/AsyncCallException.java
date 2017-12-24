package com.github.wxz.exception;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -18:20
 */
public class AsyncCallException extends RuntimeException {
    public AsyncCallException() {
        super();
    }

    public AsyncCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsyncCallException(String message) {
        super(message);
    }

    public AsyncCallException(Throwable cause) {
        super(cause);
    }
}

