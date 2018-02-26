package com.security.zap.utils.exception;

/**
 * Exception thrown when ZAP initialization fails.
 *
 */
public class ZapInitializationException extends RuntimeException {

	private static final long serialVersionUID = -2305184594319127381L;

	public ZapInitializationException(String message) {
		super(message);
	}
	
	public ZapInitializationException(Throwable e) {
		super(e);
	}
	
	public ZapInitializationException(String message, Throwable e) {
		super(message, e);
	}
	
}
