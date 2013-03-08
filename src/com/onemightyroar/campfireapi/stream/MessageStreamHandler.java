package com.onemightyroar.campfireapi.stream;

import com.onemightyroar.campfireapi.models.Message;
import com.onemightyroar.campfireapi.models.Room;
import com.onemightyroar.campfireapi.models.User;


public interface MessageStreamHandler {

    /**
     * What to do when we get a new message from the stream
     * @param t Incoming message
     */
    public void addMessage(Message m);


    /**
     * Campfire stream handler has been requested to stop.
     */
    public void stop();
    
    /**
     * User has entered the room
     */
    public void enter(User user, Room room);
    
    /**
     * User has left the room
     */
    public void leave(User user, Room room);
    
}