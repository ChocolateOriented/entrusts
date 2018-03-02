package com.entrusts.exception;

/**
 * API接口调用失败异常
 */
public class ApiException extends Exception {

	private static final long serialVersionUID = 1L;

	public ApiException() {
		super();
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}
}
