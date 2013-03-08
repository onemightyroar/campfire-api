package com.onemightyroar.campfireapi.stream;

import com.onemightyroar.campfireapi.models.Message;
import com.onemightyroar.campfireapi.models.Room;
import com.onemightyroar.campfireapi.models.User;


/**
 * The Interface MessageStreamHandler.
 */
public interface MessageStreamHandler {

    /**
     * What to do when we get a new message from the stream.
     *
     * @param m the m
     */
    void addMessage(Message m);


    /**
     * Campfire stream handler has been requested to stop.
     */
    void stop();
    
    /**
     * User has entered the room.
     *
     * @param user the user
     * @param room the room
     */
    void enter(User user, Room room);
    
    /**
     * User has left the room.
     *
     * @param user the user
     * @param room the room
     */
    void leave(User user, Room room);

    /**
     * Connection Lost.
     *
     * @param room the room
     */
    void disconnect(Room room);
    
    
}
