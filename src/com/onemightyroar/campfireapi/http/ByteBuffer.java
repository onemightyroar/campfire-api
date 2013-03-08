package com.onemightyroar.campfireapi.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/*
 * Copyright 2011 Björn Raupach

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
class ByteBuffer {
	
	private final int EMPTY_RESPONSE = 1;
	private byte[] buffer;
	
	public ByteBuffer(InputStream is) throws IOException {
		if (is == null || is.available() == EMPTY_RESPONSE) {
			buffer = new byte[0];
		} else {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] data = new byte[4096];

			for (int n; (n = is.read(data, 0, data.length)) != -1;) {
			  out.write(data, 0, n);
			}
			
			out.flush();
			buffer = out.toByteArray();
			out.close();
			is.close();
		}
	}
	
	public byte[] getByteArray() {
		return buffer;
	}
	
	public String getAsString(String charsetName) {
		if (getByteArray().length == 0) {
			return "";
		} else {
			try {
				return new String(getByteArray(), charsetName);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public InputStream getInputStream() {
		return new ByteArrayInputStream(buffer);
	}

}
