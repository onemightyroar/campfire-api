package com.onemightyroar.campfire.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.onemightyroar.campfire.api.factory.ResourceFactory;
import com.onemightyroar.campfire.api.http.HttpConnection;
import com.onemightyroar.campfire.api.http.URLBuilder;
import com.onemightyroar.campfire.api.models.Account;
import com.onemightyroar.campfire.api.models.Message;
import com.onemightyroar.campfire.api.models.Room;
import com.onemightyroar.campfire.api.models.Upload;
import com.onemightyroar.campfire.api.models.User;
import com.onemightyroar.campfire.api.stream.MessageStream;
import com.onemightyroar.campfire.api.stream.MessageStreamHandler;

public class CampfireApi {

	private final String host;
	private HttpConnection httpConnection;
	private final ResourceFactory resourceFactory;
	
	/**
	  * Constructor.
	  * @param accountName (required) your Campfire account name.
	  */
	public CampfireApi(String accountName) {
		this.host = accountName + ".campfirenow.com";
		if(accountName.equals("")) {
			throw new RuntimeException();
		}
		this.httpConnection = new HttpConnection("X", "X");
		this.resourceFactory = new ResourceFactory(this);
	}
	
	/**
	  * Constructor.
	  * @param accountName (required) your Campfire account name.
	  * @param authToken (required)
	  */
	public CampfireApi(String accountName, String authToken) {
		this.host = accountName + ".campfirenow.com";
		if(accountName.equals("")) {
			throw new RuntimeException();
		}
		this.httpConnection = new HttpConnection(authToken, "X");
		this.resourceFactory = new ResourceFactory(this);
	}
	
	/**
	  * Constructor for use with getMe(). This method is deprecated.<br><br>
	  * Instead you should use the {@link com.onemightyroar.campfire.api.CampfireApi#CampfireApi(String) Basic Constructor}
	  * and {@link com.onemightyroar.campfire.api.CampfireApi#getMe(String, String) getMe}. You can use the response
	  * with {@link com.onemightyroar.campfire.api.CampfireApi#setAuthToken(String) setAuthToken} to make additional calls.
	  * @param accountName (required) your Campfire account name.
	  * @param username (required)
	  * @param password (required) 
	  */
	@Deprecated
	public CampfireApi(String accountName, String username, String password) {
		this.host = accountName + ".campfirenow.com";
		if(accountName.equals("")) {
			throw new RuntimeException();
		}
		this.httpConnection = new HttpConnection(username, password);
		this.resourceFactory = new ResourceFactory(this);
	}
	
	public HttpConnection getConnection() {
		return this.httpConnection;
	}
	
	/**
	 * Sets the auth token to be used to authenticate http requests
	 * @param authToken
	 */
	public void setAuthToken(String authToken) {
		this.httpConnection = new HttpConnection(authToken, "X");
	}
	
	/**
	 * Get the user's account
	 * 
	 * @return the room
	 */
    public Account getAccount() {
		URLBuilder url = new URLBuilder(host, "/account.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildAccount(httpResult);
    }
	
	/**
	 * Begin a stream of messages.
	 * 
	 * @param room
	 * @param handler
	 * 
	 * @return the room
	 */
    public MessageStream messages(Long roomId, MessageStreamHandler handler)
            throws IOException {
        
        //build get params
        HttpParams getParams = new BasicHttpParams();
        
        //send request
        HttpRequestBase conn = httpConnection.buildConnection("https://streaming.campfirenow.com/room/" + Long.toString(roomId) + "/live.json", getParams);
        return new MessageStream(conn, handler, this);
    }
	
	/**
	 * Begin a stream of messages.
	 * 
	 * This method accepts a room object. It will maintain the room object's list
	 * of active and inactive users. The MessageStreamHandler's "enter" and "leave"
	 * methods will be used to let you know when users enter and leave.
	 * 
	 * @param room
	 * @param handler
	 * 
	 * @return the room
	 */
    public MessageStream messages(Room room, MessageStreamHandler handler)
            throws IOException {
        
        //build get params
        HttpParams getParams = new BasicHttpParams();
        
        //send request
        HttpRequestBase conn = httpConnection.buildConnection("https://streaming.campfirenow.com/room/" + room.getId() + "/live.json", getParams);
        return new MessageStream(conn, handler, this, room);
    }
	
	/**
	 * Get a room and its users details
	 * @param roomId
	 * @return the room
	 */
	public Room getRoom(Long roomId) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + ".json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildRoom(httpResult);
	}
	
	/**
	 * Get all the rooms for the user
	 * @return a list of rooms
	 */
	public List<Room> getRooms() {
		URLBuilder url = new URLBuilder(host, "/rooms.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildRooms(httpResult);
	}
	
	/**
	 * Get all the rooms that the user is currently in
	 * @return a list of rooms
	 */
	public List<Room> getPresence() {
		URLBuilder url = new URLBuilder(host, "/presence.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildRooms(httpResult);
	}
	
	/**
	 * posts an upload to a room
	 * @param roomId
	 * @return the upload
	 */
	public Upload postUpload(Long roomId, InputStream fileInputStream, String fileName, String mimeType) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/uploads.json");
		String httpResult = httpConnection.doPostUpload(url.toURL(), fileInputStream, fileName, mimeType);
		return resourceFactory.buildUpload(httpResult);
	}
	
	/**
	 * gets recently uploads files by room id
	 * @param roomId
	 * @return the list of uploads
	 */
	public List<Upload> getUploads(Long roomId) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/uploads.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildUploads(httpResult);
	}
	
	/**
	 * Update the name or topic of a toom
	 * @param roomId
	 * @param name The name of the room
	 * @param topic The room's topic
	 * @return success
	 */
	public boolean updateRoom(Long roomId, String name, String topic) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + ".json");
		
		JSONObject roomDetails = new JSONObject();
		JSONObject request = new JSONObject();
		try {
			roomDetails.put("name", name);
			roomDetails.put("topic", topic);
			request.put("room", roomDetails);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		boolean success = true;
		try{
			httpConnection.doPut(url.toURL(), request.toString());
		}catch(ApiException e) {
			success = false;
		}
		
		return success;
	}
	
	/**
	 * Get the recent messages from a room
	 * @param roomId
	 * @return a list of rooms
	 */
	public List<Message> getRecentMessages(Long roomId) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/recent.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildMessages(httpResult);
	}
	
	/**
	 * Get the recent messages from a room
	 * 
	 * This method takes a room object. If a message object contains a user ID that is not within the room's
	 * active or inactive user list, it will get that user and add them to the room.
	 * 
	 * @param room
	 * @return a list of rooms
	 */
	public List<Message> getRecentMessages(Room room) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(room.getId()) + "/recent.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildMessages(httpResult, room);
	}
	
	/**
	 * Gets the upload from an upload message
	 * @param roomId
	 * @param messageId
	 * @return a list of rooms
	 */
	public Upload getMessageUpload(Long roomId, Long messageId) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/messages/" + messageId + "/upload.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildUpload(httpResult);
	}
	
	/**
	 * Joins a room
	 * @param roomId
	 * @return success
	 */
	public boolean joinRoom(Long roomId) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/join.json");
		return emptyPost(url.toURL());
	}
	
	/**
	 * Leaves a room
	 * @param roomId
	 * @return success
	 */
	public boolean leaveRoom(Long roomId) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/leave.json");
		return emptyPost(url.toURL());
	}
	
	/**
	 * Locks a room
	 * @param roomId
	 * @return success
	 */
	public boolean lockRoom(Long roomId) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/lock.json");
		return emptyPost(url.toURL());
	}
	
	/**
	 * Unlocks a room
	 * @param roomId
	 * @return success
	 */
	public boolean unlockRoom(Long roomId) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/unlock.json");
		return emptyPost(url.toURL());
	}
	
	/**
	 * Searches for messages
	 * @param term The search term
	 * @return success
	 */
	public List<Message> searchMessages(String term) {
		URLBuilder url = new URLBuilder(host, "/search/" + term + ".json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildMessages(httpResult);
	}
	
	/**
	 * Posts a message
	 * @param messageId
	 * @return success
	 */
	public Message postMessage(Long roomId, String type, String message) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/speak.json");
		
		JSONObject messageDetails = new JSONObject();
		JSONObject request = new JSONObject();
		try {
			messageDetails.put("type", type);
			messageDetails.put("body", message);
			request.put("message", messageDetails);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		try{
			String httpResult = httpConnection.doPost(url.toURL(), request.toString());
			return resourceFactory.buildMessage(httpResult);
		}catch(ApiException e) {
			return null;
		}

	}
	
	/**
	 * Stars a message
	 * @param messageId
	 * @return success
	 */
	public boolean addMessageStar(Long messageId) {
		URLBuilder url = new URLBuilder(host, "/messages/" + Long.toString(messageId) + "/star.json");
		return emptyPost(url.toURL());
	}
	
	/**
	 * Unstars a message
	 * @param messageId
	 * @return success
	 */
	public boolean deleteMessageStar(Long messageId) {
		URLBuilder url = new URLBuilder(host, "/messages/" + Long.toString(messageId) + "/star.json");
		return emptyDelete(url.toURL());
	}
	
	/**
	 * Gets the days full transcript for a room
	 * @param roomId
	 * @return success
	 */
	public List<Message> getTodaysTranscript(Long roomId) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/transcript.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildMessages(httpResult);
	}
	
	/**
	 * Searches for messages
	 * @param roomId
	 * @param year
	 * @param month
	 * @param day
	 * @return success
	 */
	public List<Message> getTranscript(Long roomId, int year, int month, int day) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/transcript/"+Integer.toString(year) +"/"+Integer.toString(month)+"/"+Integer.toString(day)+".json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildMessages(httpResult, null);
	}
	
	/**
	 * Searches for messages (manages users for you)
	 * @param room
	 * @param year
	 * @param month
	 * @param day
	 * @return success
	 */
	public List<Message> getTranscript(Room room, int year, int month, int day) {
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(room.getId()) + "/transcript/"+Integer.toString(year) +"/"+Integer.toString(month)+"/"+Integer.toString(day)+".json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildMessages(httpResult, room);
	}

	
	/**
	 * Gets a user
	 * @param userId
	 * @return success
	 */
	public User getUser(Long userId) {
		URLBuilder url = new URLBuilder(host, "/users/" + Long.toString(userId) +".json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildUser(httpResult);
	}

	
	/**
	 * Gets the current user. If the AuthToken hasn't been set yet, you should use 
	 * {@link com.onemightyroar.campfire.api.CampfireApi#getMe(String, String) getMe(String, String)}
	 * @return success
	 */
	public User getMe() {
		URLBuilder url = new URLBuilder(host, "/users/me.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildUser(httpResult);
	}
	
	/**
	 * Gets the current user with the provided username and password.
	 * @return success
	 */
	public User getMe(String username, String password) {
		this.httpConnection = new HttpConnection(username, password);
		
		URLBuilder url = new URLBuilder(host, "/users/me.json");
		String httpResult = httpConnection.doGet(url.toURL());
		return resourceFactory.buildUser(httpResult);
	}


	
	/**
	 * performs an empty post to a specified URL
	 * @return success
	 */
	private boolean emptyPost(URL url) {
		
		boolean success = true;
		try{
			httpConnection.doPost(url, "-");
		}catch(ApiException e) {
			success = false;
		}
		
		return success;
	}

	
	/**
	 * performs an empty delete to a specified URL
	 * @return success
	 */
	private boolean emptyDelete(URL url) {
		
		boolean success = true;
		try{
			httpConnection.doDelete(url);
		}catch(ApiException e) {
			success = false;
		}
		
		return success;
	}

	
	/**
	 * pings the room to keep the user active
	 * @return success
	 */
	public void pingRoom(Long roomId) {
		
		URLBuilder url = new URLBuilder(host, "/room/" + Long.toString(roomId) + "/ping");
		emptyPost(url.toURL());
		//httpConnection.doGet(url.toURL());
		
	}
	
	
}
