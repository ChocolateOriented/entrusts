package com.entrusts.exception;

/**
 * WEB API异常
 */
public class WebApiException extends Exception {

	private static final long serialVersionUID = 1L;

	public WebApiException() {
		super();
	}

	public WebApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebApiException(String message) {
		super(message);
	}

	public WebApiException(Throwable cause) {
		super(cause);
	}
}
