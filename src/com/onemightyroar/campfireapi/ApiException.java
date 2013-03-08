package com.onemightyroar.campfireapi;

/**
 * ApiException.
 *
 * @author brianmuse
 */
public class ApiException extends RuntimeException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The response code. */
	private final int mResponseCode;
	
	/**
	 * Instantiates a new api exception.
	 *
	 * @param responseCode the response code
	 * @param message the message
	 */
	public ApiException(final int responseCode, final String message) {
		super(responseCode + ("".equals(message) ? "" : " - " + message));
		this.mResponseCode = responseCode;
	}
	
	/**
	 * Gets the response code.
	 *
	 * @return the response code
	 */
	public int getResponseCode() {
		return this.mResponseCode;
	}
	
}
