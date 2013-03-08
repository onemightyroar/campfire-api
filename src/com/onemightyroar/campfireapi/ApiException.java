/**
 * Campfire Api - A Java library for Campfire from 37Signals
 *
 * @author		brianmuse
 * @copyright	2013 One Mighty Roar
 * @link		https://github.com/onemightyroar/campfire-api
 * @license		https://github.com/onemightyroar/campfire-api/blob/master/LICENSE.md
 * @version		1.0.0
 */
	
package com.onemightyroar.campfireapi;

/**
 * ApiException
 * 
 * @author brianmuse
 */
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
