package com.onemightyroar.campfireapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The Class StreamUtils.
 */
public class StreamUtils {
	
    /** The buffer reader size **/
    private static final int BUFFER_READER_SIZE = 8 * 1024;
    
	/**
	 * Convert stream to string.
	 *
	 * @param is the is
	 * @return the string
	 */
	public static String convertStreamToString(final InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is), BUFFER_READER_SIZE);
		StringBuilder sb = new StringBuilder();

		try {
	        String line = null;
	        
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
            is.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}

}
