package com.onemightyroar.campfireapi.factory;

/**
 * Translator for JSON Room fields.
 */
public class JsonRoom {
    /** JSON key for the room object */
    public static final String OBJECT_NAME = "room";
    
    /** JSON key for the rooms array */
    public static final String OBJECT_NAME_PLURAL = "rooms";
    
    /** JSON key for the room's id */
    public static final String ID = "id";
    
    /** JSON key for the rooms name */
    public static final String NAME = "name";
    
    /** JSON key for the room's membership limit */
    public static final String MEMBERSHIP_LIMIT = "membership_limit";
    
    /** JSON key for the room's topic */
    public static final String TOPIC = "topic";
    
    /** JSON key for whether the room can have guests */
    public static final String OPEN_TO_GUESTS = "open_to_guests";
    
    /** JSON key for if the room is full */
    public static final String FULL = "full";
    
    /** JSON key for active_token_value */
    public static final String ACTIVE_TOKEN_VALUE = "active_token_value";
    
    /** JSON key for the created at timestamp */
    public static final String CREATED_AT = "created_at";
    
    /** JSON key for the updated at timestamp */
    public static final String UPDATED_AT = "updated_at";
}
