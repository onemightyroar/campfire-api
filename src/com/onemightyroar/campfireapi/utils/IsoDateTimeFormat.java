package com.onemightyroar.campfireapi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class IsoDateTimeFormat {
	
	private static final SimpleDateFormat isodatetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
	
	public static synchronized Date parse(String source) {
		if (source == null) {
			return null;
		} else if (source.endsWith("Z")) {
			source = source.substring(0, source.length() - 1) + "GMT-00:00";
		} else {
			int inset = 6;
			String s0 = source.substring( 0, source.length() - inset );
			String s1 = source.substring( source.length() - inset, source.length() );
			source = s0 + "GMT" + s1;
		}
		try {
			return isodatetime.parse(source);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static synchronized String format(Date date) {
		return isodatetime.format(date);
	}
	

}
