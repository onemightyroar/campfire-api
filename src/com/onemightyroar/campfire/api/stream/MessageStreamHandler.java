package com.onemightyroar.campfire.api.stream;

import com.onemightyroar.campfire.api.models.Message;
import com.onemightyroar.campfire.api.models.Room;
import com.onemightyroar.campfire.api.models.User;


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