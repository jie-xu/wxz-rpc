package com.github.wxz.rpc.exception;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -18:20
 */
public class RejectResponeException extends RuntimeException {
    public RejectResponeException() {
        super();
    }

    public RejectResponeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RejectResponeException(String message) {
        super(message);
    }

    public RejectResponeException(Throwable cause) {
        super(cause);
    }
}

