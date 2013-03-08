package com.onemightyroar.campfireapi.factory;

/**
 * Translator for JSON Message fields.
 */
public class JsonMessage {
    /** JSON key for the message object */
    public static final String OBJECT_NAME = "message";
    
    /** JSON key for the messages array */
    public static final String OBJECT_NAME_PLURAL = "messages";
    
    /** JSON key for the message's id */
    public static final String ID = "id";
    
    /** JSON key for the room_id */
    public static final String ROOM_ID = "room_id";
    
    /** JSON key for the user_id body */
    public static final String USER_ID = "user_id";
    
    /** JSON key for the message body */
    public static final String BODY = "body";
    
    /** JSON key for whether the message is starred */
    public static final String STARRED = "starred";
    
    /** JSON key for the message type */
    public static final String TYPE = "type";
    
    /** JSON key for the created at timestamp */
    public static final String CREATED_AT = "created_at";
}
