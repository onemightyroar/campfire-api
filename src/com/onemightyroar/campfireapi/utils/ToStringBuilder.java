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

package com.onemightyroar.campfireapi.utils;

/**
* A stripped down version of Apache Commons ToStringBuilder.
*/
public class ToStringBuilder {
	
    /** The this.mBuffer. */
    private StringBuilder mBuffer = new StringBuilder();
    
    /** The fields appended. */
    private boolean mFieldsAppended;

    /**
     * Instantiates a new to string builder.
     *
     * @param target the target
     */
    public ToStringBuilder(final Object target) {
    	appendCanonicalName(target);
        appendHashCode(target);
    }

    /**
     * Append canonical name.
     *
     * @param obj the obj
     */
    private void appendCanonicalName(final Object obj) {
    	this.mBuffer.append(obj.getClass().getCanonicalName());
    }
    
    /**
     * Append hash code.
     *
     * @param target the target
     */
    private void appendHashCode(final Object target) {
        this.mBuffer.append('@');
        this.mBuffer.append(Integer.toHexString(target.hashCode()));
    }

    /**
     * @return The object as a string
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (this.mFieldsAppended) {
            this.mBuffer.append(']');
        }
        return this.mBuffer.toString();
    }

    /**
     * Append.
     *
     * @param fieldName the field name
     * @param value the value
     * @return the to string builder
     */
    public ToStringBuilder append(final String fieldName, final Object value) {
    	if (value == null) {
    		return this.append(fieldName, "nil");
    	} else {
    		return this.append(fieldName, value.toString());
    	}
        
    }

    /**
     * Append.
     *
     * @param fieldName the field name
     * @param value the value
     * @return the to string builder
     */
    public ToStringBuilder append(final String fieldName, final String value) {
        if (!this.mFieldsAppended) {
            this.mBuffer.append('[');
            this.mFieldsAppended = true;
        } else {
            this.mBuffer.append(' ');
        }
        this.mBuffer.append(fieldName).append('=').append(value);
        return this;
    }
}
