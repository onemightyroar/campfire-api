package com.onemightyroar.campfire.api.http;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


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
public class URLBuilder {
	
	private final String host;
	private final String path;
	private final List<FieldValuePair> fieldValuePairs;
	
	public URLBuilder(String host, String path) {
		this.host = host;
		this.path = path;
		this.fieldValuePairs = new LinkedList<URLBuilder.FieldValuePair>();
	}
	
	public URLBuilder addFieldValuePair(String field, Object value) {
		if (value != null) {
			return addFieldValuePair(new FieldValuePair(field, value));
		} else {
			return this;
		}
	}
	
	private URLBuilder addFieldValuePair(FieldValuePair fieldValuePair) {
		fieldValuePairs.add(fieldValuePair);
		return this;
	}
	
	public URI toURI() {
		String query = null;
		if (!fieldValuePairs.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (FieldValuePair pair : fieldValuePairs) {
				sb.append(pair.getField())
					.append("=")
					.append(pair.getValue())
					.append("&");
			}
			sb.deleteCharAt(sb.length()-1); // chop last ampersand
			query = sb.toString();
		}
		
		URI uri = null;
		try {
			uri = new URI("https", null, host, -1, path, query, null);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return uri;
	}
	
	public URL toURL() {
		try {
			return toURI().toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public class FieldValuePair {
		
		final String field;
		final Object value;
		
		public FieldValuePair(String field, Object value) {
			this.field = field;
			this.value = value;
		}
		
		public String getField() {
			return field;
		}
		
		public Object getValue() {
			return value;
		}
		
		public String toString() {
			return field + "=" + value;
		}
		
	}
	
}
