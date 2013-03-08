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

package com.onemightyroar.campfireapi.http;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class URLBuilder.
 */
public class URLBuilder {
	
	/** The this.mHost. */
	private final String mHost;
	
	/** The path. */
	private final String mPath;
	
	/** The field value pairs. */
	private final List<FieldValuePair> mFieldValuePairs;
	
	/**
	 * Instantiates a new uRL builder.
	 *
	 * @param host The host
	 * @param path the path
	 */
	public URLBuilder(final String host, final String path) {
		this.mHost = host;
		this.mPath = path;
		this.mFieldValuePairs = new LinkedList<URLBuilder.FieldValuePair>();
	}
	
	/**
	 * Adds the field value pair.
	 *
	 * @param field the field
	 * @param value the value
	 * @return the uRL builder
	 */
	public URLBuilder addFieldValuePair(final String field, final Object value) {
		if (value != null) {
			return this.addFieldValuePair(new FieldValuePair(field, value));
		} else {
			return this;
		}
	}
	
	/**
	 * Adds the field value pair.
	 *
	 * @param fieldValuePair the field value pair
	 * @return the uRL builder
	 */
	private URLBuilder addFieldValuePair(final FieldValuePair fieldValuePair) {
		this.mFieldValuePairs.add(fieldValuePair);
		return this;
	}
	
	/**
	 * To uri.
	 *
	 * @return the uri
	 */
	public URI toURI() {
		String query = null;
		if (!this.mFieldValuePairs.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (FieldValuePair pair : this.mFieldValuePairs) {
				sb.append(pair.getField())
					.append("=")
					.append(pair.getValue())
					.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
			query = sb.toString();
		}
		
		URI uri = null;
		try {
			uri = new URI("https", null, this.mHost, -1, this.mPath, query, null);
		} catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return uri;
	}
	
	/**
	 * To url.
	 *
	 * @return the url
	 */
	public URL toURL() {
		try {
			return this.toURI().toURL();
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * The Class FieldValuePair.
	 */
	public class FieldValuePair {
		
		/** The field. */
		final String mField;
		
		/** The value. */
		final Object mValue;
		
		/**
		 * Instantiates a new field value pair.
		 *
		 * @param field the field
		 * @param value the value
		 */
		public FieldValuePair(final String field, final Object value) {
			this.mField = field;
			this.mValue = value;
		}
		
		/**
		 * Gets the field.
		 *
		 * @return the field
		 */
		public String getField() {
			return this.mField;
		}
		
		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public Object getValue() {
			return this.mValue;
		}
		
		/**
		 * @return The object as a string
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return this.mField + "=" + this.mValue;
		}
		
	}
	
}
