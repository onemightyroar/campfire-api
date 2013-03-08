package com.onemightyroar.campfireapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.onemightyroar.campfireapi.factory.ResourceFactory;
import com.onemightyroar.campfireapi.http.JsonHttpConnection;
import com.onemightyroar.campfireapi.http.URLBuilder;
import com.onemightyroar.campfireapi.models.Account;
import com.onemightyroar.campfireapi.models.Message;
import com.onemightyroar.campfireapi.models.Room;
import com.onemightyroar.campfireapi.models.Upload;
import com.onemightyroar.campfireapi.models.User;
import com.onemightyroar.campfireapi.stream.MessageStream;
import com.onemightyroar.campfireapi.stream.MessageStreamHandler;

/**
 * CampfireApi
 * 
 * Java wrapper for the 37Signal Campfire APIs.
 *
 * @author brianmuse
 */
public class CampfireApi {

    /** The campfire base URI */
    protected static final String CAMPFIRE_DOMAIN = "{0}.campfirenow.com";
    
    /** The campfire streaming URL */
    protected static final String CAMPFIRE_STREAM_URL = "https://streaming.campfirenow.com/room/{0}/live.json";
    
    /** Endpoint for accounts **/
    protected static final String CAMPFIRE_ACCOUNT_URI = "/account.json";
    
    /** Endpoint for rooms **/
    protected static final String CAMPFIRE_ROOMS_URI = "/rooms.json";
    
    /** Endpoint for rooms **/
    protected static final String CAMPFIRE_PRESENCE_URI = "/presence.json";
    
    /** Endpoint for a specific room **/
    protected static final String CAMPFIRE_ROOM_URI = "/room/{0}.json";
    
    /** Endpoint to join a room **/
    protected static final String CAMPFIRE_ROOM_JOIN_URI = "/room/{0}/join.json";
    
    /** Endpoint to leave a room **/
    protected static final String CAMPFIRE_ROOM_LEAVE_URI = "/room/{0}/leave.json";
    
    /** Endpoint to lock a room **/
    protected static final String CAMPFIRE_ROOM_LOCK_URI = "/room/{0}/lock.json";
    
    /** Endpoint to unlock a room **/
    protected static final String CAMPFIRE_ROOM_UNLOCK_URI = "/room/{0}/unlock.json";
    
    /** Endpoint to post a message to a room **/
    protected static final String CAMPFIRE_ROOM_SPEAK_URI = "/room/{0}/speak.json";
    
    /** Endpoint for a specific room's uploads **/
    protected static final String CAMPFIRE_ROOM_RECENT_URI = "/room/{0}/recent.json";
    
    /**
     * Endpoint to get a transcript for a room for a specific date
     * <p>
     * /room/:roomId/transcript/:year/:month/:day.json
     */
    protected static final String CAMPFIRE_ROOM_TRANSCRIPT_URI = "/room/{0}/transcript/{1}/{2}/{3}.json";
    
    /** Endpoint to get a room's transcript for a specific date **/
    protected static final String CAMPFIRE_ROOM_TRANSCRIPT_DATE_URI = "/room/{0}/transcript.json";
    
    /** Endpoint for a specific room's uploads **/
    protected static final String CAMPFIRE_ROOM_UPLOAD_URI = "/room/{0}/uploads.json";
    
    /** Endpoint for a specific message upload **/
    protected static final String CAMPFIRE_MESSAGE_UPLOAD_URI = "/room/{0}/messages/{1}/upload.json";
    
    /** Endpoint to ping a room to keep the user active **/
    protected static final String CAMPFIRE_ROOM_PING_URI = "/room/{0}/ping";
    
    /** Endpoint for highlighting messages **/
    protected static final String CAMPFIRE_MESSAGE_STAR_URI = "/messages/{0}/star.json";
    
    /** Endpoint for a specific user **/
    protected static final String CAMPFIRE_USER_URI = "/users/{0}.json";
    
    /** Endpoint for the current user **/
    protected static final String CAMPFIRE_ME_URI = "/users/me.json";
    
    /** Endpoint for room search **/
    protected static final String CAMPFIRE_SEARCH_URI = "/search/{0}.json";
    
    /** Campfire param to get messages with id > the provided id **/
    protected static final String CAMPFIRE_SINCE_MESSAGE_PARAM = "since_message_id";
    
	/** The host. */
	private final String mHost;
	
	/** The http connection. */
	private final JsonHttpConnection mHttpConnection;
	
	/** The resource factory. */
	private final ResourceFactory mResourceFactory;
	
	
	/**
	  * Constructor.
	  * @param accountName (required) your Campfire account name.
	  * @param authToken (required)
	  */
	public CampfireApi(final String accountName, final String authToken) {
		this.mHost = MessageFormat.format(CAMPFIRE_DOMAIN, accountName);
		if ("".equals(accountName)) {
			throw new RuntimeException();
		}
		this.mHttpConnection = new JsonHttpConnection(authToken, "X");
		this.mResourceFactory = new ResourceFactory(this);
	}
	
	/**
	  * Constructor.
	  * @param accountName (required) your Campfire account name.
	  * @param username (required)
	  * @param password (required) 
	  */
	public CampfireApi(final String accountName, final String username, final String password) {
        this.mHost = MessageFormat.format(CAMPFIRE_DOMAIN, accountName);
		if ("".equals(accountName)) {
			throw new RuntimeException();
		}
		this.mHttpConnection = new JsonHttpConnection(username, password);
		this.mResourceFactory = new ResourceFactory(this);
	}
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public JsonHttpConnection getConnection() {
		return this.mHttpConnection;
	}
	
	/**
	 * Get the user's account.
	 *
	 * @return the room
	 */
    public Account getAccount() {
		URLBuilder url = new URLBuilder(this.mHost, CAMPFIRE_ACCOUNT_URI);
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildAccount(httpResult);
    }
	
	/**
	 * Begin a stream of messages.
	 *
	 * @param roomId the room id
	 * @param handler the handler
	 * @return the room
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public MessageStream messages(final Long roomId, final MessageStreamHandler handler)
            throws IOException {
        
        //build get params
        HttpParams getParams = new BasicHttpParams();
        
        //send request
        HttpRequestBase conn = this.mHttpConnection.buildConnection(
                MessageFormat.format(CAMPFIRE_STREAM_URL, Long.toString(roomId)), getParams);
        
        return new MessageStream(conn, handler, this);
    }
	
	/**
	 * Begin a stream of messages.
	 * 
	 * This method accepts a room object. It will maintain the room object's list
	 * of active and inactive users. The MessageStreamHandler's "enter" and "leave"
	 * methods will be used to let you know when users enter and leave.
	 *
	 * @param room the room
	 * @param handler the handler
	 * @return the room
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public MessageStream messages(final Room room, final MessageStreamHandler handler)
            throws IOException {
        
        //build get params
        HttpParams getParams = new BasicHttpParams();
        
        //send request
        HttpRequestBase conn = this.mHttpConnection.buildConnection(
                    MessageFormat.format(CAMPFIRE_STREAM_URL, Long.toString(room.getId())), getParams);
        
        return new MessageStream(conn, handler, this, room);
    }
	
	/**
	 * Get a room and its users details.
	 *
	 * @param roomId the room id
	 * @return the room
	 */
	public Room getRoom(final Long roomId) {
		URLBuilder url = new URLBuilder(this.mHost, MessageFormat.format(CAMPFIRE_ROOM_URI, Long.toString(roomId)));
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildRoom(httpResult);
	}
	
	/**
	 * Get all the rooms for the user.
	 *
	 * @return a list of rooms
	 */
	public List<Room> getRooms() {
		URLBuilder url = new URLBuilder(this.mHost, CAMPFIRE_ROOMS_URI);
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildRooms(httpResult);
	}
	
	/**
	 * Get all the rooms that the user is currently in.
	 *
	 * @return a list of rooms
	 */
	public List<Room> getPresence() {
		URLBuilder url = new URLBuilder(this.mHost, CAMPFIRE_PRESENCE_URI);
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildRooms(httpResult);
	}
	
	/**
	 * posts an upload to a room.
	 *
	 * @param roomId the room id
	 * @param fileInputStream the file input stream
	 * @param fileName the file name
	 * @param mimeType the mime type
	 * @return the upload
	 */
	public Upload postUpload(final Long roomId, final InputStream fileInputStream, 
	        final String fileName, final String mimeType) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_UPLOAD_URI, Long.toString(roomId)));
		
		String httpResult = this.mHttpConnection.doPostUpload(url.toURL(), fileInputStream, fileName, mimeType);
		return httpResult == null ? null : this.mResourceFactory.buildUpload(httpResult);
	}
	
	/**
	 * gets recently uploads files by room id.
	 *
	 * @param roomId the room id
	 * @return the list of uploads
	 */
	public List<Upload> getUploads(final Long roomId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_UPLOAD_URI, Long.toString(roomId)));
		
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildUploads(httpResult);
	}
	
	/**
	 * Update the name or topic of a toom.
	 *
	 * @param roomId the room id
	 * @param name The name of the room
	 * @param topic The room's topic
	 * @return success
	 */
	public boolean updateRoom(final Long roomId, final String name, final String topic) {
		URLBuilder url = new URLBuilder(this.mHost, MessageFormat.format(CAMPFIRE_ROOM_URI, Long.toString(roomId)));
		
		JSONObject roomDetails = new JSONObject();
		JSONObject request = new JSONObject();
		
		Boolean success = true;
		try {
			roomDetails.put("name", name);
			roomDetails.put("topic", topic);
			request.put("room", roomDetails);

            this.mHttpConnection.doPut(url.toURL(), request.toString());
		} catch (final JSONException e) {
			e.printStackTrace();
			success = false;
		} catch (final ApiException e) {
            e.printStackTrace();
            success = false;
		}
		
		return success;
	}
	
	/**
	 * Get the recent messages from a room.
	 *
	 * @param roomId the room id
	 * @return a list of rooms
	 */
	public List<Message> getRecentMessages(final Long roomId) {
		return this.getRecentMessages(roomId, null);
	}
	
	/**
	 * Get the recent messages from a room
	 * 
	 * This method takes a room object. If a message object contains a user ID that is not within the room's
	 * active or inactive user list, it will get that user and add them to the room.
	 *
	 * @param room the room
	 * @return a list of rooms
	 */
	public List<Message> getRecentMessages(final Room room) {
		return this.getRecentMessages(room, null);
	}
	
	/**
	 * Get the recent messages from a room that occured after a specific message ID
	 * 
	 * This method takes a room object. If a message object contains a user ID that is not within the room's
	 * active or inactive user list, it will get that user and add them to the room.
	 *
	 * @param roomId the room id
	 * @param messageId the message id
	 * @return a list of rooms
	 */
	public List<Message> getRecentMessages(final Long roomId, final Long messageId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_RECENT_URI, Long.toString(roomId)));
		
		if (messageId != null) {
			url.addFieldValuePair(CAMPFIRE_SINCE_MESSAGE_PARAM, Long.toString(messageId));
		}
		
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildMessages(httpResult);
	}
	
	/**
	 * Get the recent messages from a room
	 * 
	 * This method takes a room object. If a message object contains a user ID that is not within the room's
	 * active or inactive user list, it will get that user and add them to the room.
	 *
	 * @param room the room
	 * @param messageId the message id
	 * @return a list of rooms
	 */
	public List<Message> getRecentMessages(final Room room, final Long messageId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_RECENT_URI, Long.toString(room.getId())));
		
		if (messageId != null) {
			url.addFieldValuePair(CAMPFIRE_SINCE_MESSAGE_PARAM, Long.toString(messageId));
		}
		
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildMessages(httpResult, room);
	}
	
	/**
	 * Gets the upload from an upload message.
	 *
	 * @param roomId the room id
	 * @param messageId the message id
	 * @return a list of rooms
	 */
	public Upload getMessageUpload(final Long roomId, final Long messageId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_MESSAGE_UPLOAD_URI, Long.toString(roomId), messageId));
		
		try {
			String httpResult = this.mHttpConnection.doGet(url.toURL());
			return httpResult == null ? null : this.mResourceFactory.buildUpload(httpResult);
		} catch (final ApiException e) {
			if (e.getResponseCode() == JsonHttpConnection.NOT_FOUND) {
				return null;
			} else {
				throw e;
			}
		}	
	}
	
	/**
	 * Joins a room.
	 *
	 * @param roomId the room id
	 * @return success
	 */
	public boolean joinRoom(final Long roomId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_JOIN_URI, Long.toString(roomId)));
		return emptyPost(url.toURL());
	}
	
	/**
	 * Leaves a room.
	 *
	 * @param roomId the room id
	 * @return success
	 */
	public boolean leaveRoom(final Long roomId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_LEAVE_URI, Long.toString(roomId)));
		return emptyPost(url.toURL());
	}
	
	/**
	 * Locks a room.
	 *
	 * @param roomId the room id
	 * @return success
	 */
	public boolean lockRoom(final Long roomId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_LOCK_URI, Long.toString(roomId)));
		return emptyPost(url.toURL());
	}
	
	/**
	 * Unlocks a room.
	 *
	 * @param roomId the room id
	 * @return success
	 */
	public boolean unlockRoom(final Long roomId) {
        URLBuilder url = new URLBuilder(this.mHost, 
                MessageFormat.format(CAMPFIRE_ROOM_UNLOCK_URI, Long.toString(roomId)));
		return emptyPost(url.toURL());
	}
	
	/**
	 * Searches for messages.
	 *
	 * @param term The search term
	 * @return success
	 */
	public List<Message> searchMessages(final String term) {
        URLBuilder url = new URLBuilder(this.mHost, 
                MessageFormat.format(CAMPFIRE_SEARCH_URI, term));
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildMessages(httpResult);
	}
	
	/**
	 * Posts a message.
	 *
	 * @param roomId the room id
	 * @param type the type
	 * @param message the message
	 * @return success
	 */
	public Message postMessage(final Long roomId, final String type, final String message) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_SPEAK_URI, Long.toString(roomId)));
		
		JSONObject messageDetails = new JSONObject();
		JSONObject request = new JSONObject();
		try {
			messageDetails.put("type", type);
			messageDetails.put("body", message);
			request.put("message", messageDetails);
			
            String httpResult = this.mHttpConnection.doPost(url.toURL(), request.toString());
            
            return this.mResourceFactory.buildMessage(httpResult);
		} catch (final JSONException e) {
			e.printStackTrace();
		} catch (final ApiException e) {
            e.printStackTrace();
		}
		
        return null;
	}
	
	/**
	 * Stars a message.
	 *
	 * @param messageId the message id
	 * @return success
	 */
	public boolean addMessageStar(final Long messageId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_MESSAGE_STAR_URI, Long.toString(messageId)));
		return emptyPost(url.toURL());
	}
	
	/**
	 * Unstars a message.
	 *
	 * @param messageId the message id
	 * @return success
	 */
	public boolean deleteMessageStar(final Long messageId) {
        URLBuilder url = new URLBuilder(this.mHost, 
                MessageFormat.format(CAMPFIRE_MESSAGE_STAR_URI, Long.toString(messageId)));
		return emptyDelete(url.toURL());
	}
	
	/**
	 * Gets the days full transcript for a room.
	 *
	 * @param roomId the room id
	 * @return success
	 */
	public List<Message> getTodaysTranscript(final Long roomId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_TRANSCRIPT_URI, Long.toString(roomId)));
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildMessages(httpResult);
	}
	
	/**
	 * Searches for messages.
	 *
	 * @param roomId the room id
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @return success
	 */
	public List<Message> getTranscript(final Long roomId, final int year, final int month, final int day) {
		URLBuilder url = new URLBuilder(this.mHost,
                MessageFormat.format(CAMPFIRE_ROOM_TRANSCRIPT_URI, Long.toString(roomId), 
                        Integer.toString(year), Integer.toString(month), Integer.toString(day)));
		
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildMessages(httpResult, null);
	}
	
	/**
	 * Searches for messages (manages users for you).
	 *
	 * @param room the room
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @return success
	 */
	public List<Message> getTranscript(final Room room, final int year, final int month, final int day) {
		URLBuilder url = new URLBuilder(this.mHost,
                MessageFormat.format(CAMPFIRE_ROOM_TRANSCRIPT_URI, Long.toString(room.getId()), 
                        Integer.toString(year), Integer.toString(month), Integer.toString(day)));
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildMessages(httpResult, room);
	}

	
	/**
	 * Gets a user.
	 *
	 * @param userId the user id
	 * @return success
	 */
	public User getUser(final Long userId) {
		URLBuilder url = new URLBuilder(this.mHost, MessageFormat.format(CAMPFIRE_USER_URI, Long.toString(userId)));
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildUser(httpResult);
	}

	
	/**
	 * Gets the current user.
	 *
	 * @return success
	 */
	public User getMe() {
		URLBuilder url = new URLBuilder(this.mHost, CAMPFIRE_ME_URI);
		String httpResult = this.mHttpConnection.doGet(url.toURL());
		return httpResult == null ? null : this.mResourceFactory.buildUser(httpResult);
	}


	
	/**
	 * performs an empty post to a specified URL.
	 *
	 * @param url the url
	 * @return success
	 */
	private boolean emptyPost(final URL url) {
		
		boolean success = true;
		try {
		    // Send a hyphen '-' to avoid a bug with sending an empty message
			this.mHttpConnection.doPost(url, "-");
		} catch (final ApiException e) {
			success = false;
		}
		
		return success;
	}

	
	/**
	 * performs an empty delete to a specified URL.
	 *
	 * @param url the url
	 * @return success
	 */
	private boolean emptyDelete(final URL url) {
		
		boolean success = true;
		try {
			this.mHttpConnection.doDelete(url);
		} catch (final ApiException e) {
			success = false;
		}
		
		return success;
	}

	
	/**
	 * pings the room to keep the user active.
	 *
	 * @param roomId the room id
	 */
	public void pingRoom(final Long roomId) {
		URLBuilder url = new URLBuilder(this.mHost, 
		        MessageFormat.format(CAMPFIRE_ROOM_PING_URI, Long.toString(roomId)));
		this.emptyPost(url.toURL());
	}
	
	
}
