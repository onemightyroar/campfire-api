package com.onemightyroar.campfireapi;

public class ApiException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final int responseCode;
	
	public ApiException(int responseCode, String message) {
		super(responseCode + (message.equals("") ? "" : " - " + message));
		this.responseCode = responseCode;
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	
}
