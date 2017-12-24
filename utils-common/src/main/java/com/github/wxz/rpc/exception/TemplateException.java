package com.github.wxz.rpc.exception;
/**
 * @author xianzhi.wang
 * @date 2017/12/24 -17:20
 */
public class TemplateException extends RuntimeException {

	public TemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(Throwable cause) {
		super(cause);
	}

}
