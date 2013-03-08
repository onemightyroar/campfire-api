package com.onemightyroar.campfireapi.factory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onemightyroar.campfireapi.CampfireApi;
import com.onemightyroar.campfireapi.models.Account;
import com.onemightyroar.campfireapi.models.Message;
import com.onemightyroar.campfireapi.models.MessageType;
import com.onemightyroar.campfireapi.models.Room;
import com.onemightyroar.campfireapi.models.Tweet;
import com.onemightyroar.campfireapi.models.Upload;
import com.onemightyroar.campfireapi.models.User;

/**
 * ResourceFactory
 * 
 * Factory class for building Campfire models from JSON.
 *
 * @author brianmuse
 */
public class ResourceFactory {

    /** The DATETIME format coming in from the API to translate to a {@link java.util.Date} */
    private static final String API_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss Z";
    
    /** The api. */
	private CampfireApi mApi;
	
	/**
	 * Constructor method.
	 *
	 * @param api the api
	 */
	public ResourceFactory(final CampfireApi api) {
		this.mApi = api;
	}
	
	/**
	 * Builds the account.
	 *
	 * @param httpResult the http result
	 * @return the account
	 */
	public Account buildAccount(final String httpResult) {
		Account account = new Account();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject accountJSON = json.getJSONObject(JsonAccount.OBJECT_NAME);
			account.setId(accountJSON.getLong(JsonAccount.ID));
			account.setName(accountJSON.getString(JsonAccount.NAME));
			account.setSubdomain(accountJSON.getString(JsonAccount.SUBDOMAIN));
			account.setPlan(accountJSON.getString(JsonAccount.PLAN));
			account.setOwnerId(accountJSON.getLong(JsonAccount.OWNER_ID));
			account.setTimezone(accountJSON.getString(JsonAccount.TIME_ZONE));
			account.setStorage(accountJSON.getInt(JsonAccount.STORAGE));
			account.setUpdatedAt(new SimpleDateFormat(API_TIME_FORMAT, 
			        Locale.US).parse(accountJSON.getString(JsonAccount.UPDATED_AT)));
			account.setCreatedAt(new SimpleDateFormat(API_TIME_FORMAT,
			        Locale.US).parse(accountJSON.getString(JsonAccount.CREATED_AT)));
			account.setJSON(accountJSON);
		} catch (final JSONException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return account;
	}
	
	/**
	 * Builds the rooms.
	 *
	 * @param httpResult the http result
	 * @return the list
	 */
	public List<Room> buildRooms(final String httpResult) {
		List<Room> rooms = new ArrayList<Room>();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONArray roomsJSON = json.getJSONArray(JsonRoom.OBJECT_NAME_PLURAL);
			
			for (int i = 0; i < roomsJSON.length(); i = i + 1) {
				rooms.add(buildRoom(roomsJSON.getJSONObject(i)));
			}
			
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return rooms;
	}
	
	/**
	 * Builds the room.
	 *
	 * @param httpResult the http result
	 * @return the room
	 */
	public Room buildRoom(final String httpResult) {
		Room room = new Room();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject roomJSON = json.getJSONObject(JsonRoom.OBJECT_NAME);
			
			room = this.buildRoom(roomJSON);
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return room;
	}
	
	/**
	 * Builds the room.
	 *
	 * @param roomJSON the room json
	 * @return the room
	 */
	public Room buildRoom(final JSONObject roomJSON) {
		Room room = new Room();
		
		try {
			room.setId(roomJSON.getLong(JsonRoom.ID));
			room.setName(roomJSON.getString(JsonRoom.NAME));
			room.setMembershipLimit(roomJSON.getInt(JsonRoom.MEMBERSHIP_LIMIT));
			room.setTopic(roomJSON.getString(JsonRoom.TOPIC));
			room.setOpenToGuests(roomJSON.has(JsonRoom.OPEN_TO_GUESTS) 
			        ? roomJSON.getBoolean(JsonRoom.OPEN_TO_GUESTS) : false);
			room.setFull(roomJSON.has(JsonRoom.FULL) ? roomJSON.getBoolean(JsonRoom.FULL) : false);
			room.setActiveTokenValue(roomJSON.has(JsonRoom.ACTIVE_TOKEN_VALUE) 
			        ? roomJSON.getString(JsonRoom.ACTIVE_TOKEN_VALUE) : "");
			room.setJSON(roomJSON);
			
			if (roomJSON.has(JsonUser.OBJECT_NAME_PLURAL)) {
				JSONArray usersJSON = roomJSON.getJSONArray(JsonUser.OBJECT_NAME_PLURAL);
				for (int i = 0; i < usersJSON.length(); i = i + 1) {
					room.addActiveUser(buildUser(usersJSON.getJSONObject(i)));
				}
			}

			room.setUpdatedAt(new SimpleDateFormat(API_TIME_FORMAT, 
			        Locale.US).parse(roomJSON.getString(JsonRoom.UPDATED_AT)));
			room.setCreatedAt(new SimpleDateFormat(API_TIME_FORMAT, 
			        Locale.US).parse(roomJSON.getString(JsonRoom.CREATED_AT)));
			
		} catch (final JSONException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return room;
	}
	
	/**
	 * Builds the messages.
	 *
	 * @param httpResult the http result
	 * @return the list
	 */
	public List<Message> buildMessages(final String httpResult) {
		return this.buildMessages(httpResult, null);
	}
	
	/**
	 * Builds the messages.
	 *
	 * @param httpResult the http result
	 * @param room the room
	 * @return the list
	 */
	public List<Message> buildMessages(final String httpResult, final Room room) {
		List<Message> messages = new ArrayList<Message>();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONArray messagesJSON = json.getJSONArray(JsonMessage.OBJECT_NAME_PLURAL);
			
			for (int i = 0; i < messagesJSON.length(); i = i + 1) {
				messages.add(buildMessage(messagesJSON.getJSONObject(i), room));
			}
			
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	/**
	 * Builds the message.
	 *
	 * @param httpResult the http result
	 * @return the message
	 */
	public Message buildMessage(final String httpResult) {
		return this.buildMessage(httpResult, null);
	}
	
	/**
	 * Builds the message.
	 *
	 * @param httpResult the http result
	 * @param room the room
	 * @return the message
	 */
	public Message buildMessage(final String httpResult, final Room room) {
		Message message = new Message();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject messageJSON = json.getJSONObject(JsonMessage.OBJECT_NAME);
			
			message = this.buildMessage(messageJSON, room);
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	/**
	 * Builds the message.
	 *
	 * @param messageJSON the message json
	 * @return the message
	 */
	public Message buildMessage(final JSONObject messageJSON) {
		return this.buildMessage(messageJSON, null);
	}
	
	/**
	 * Builds the message.
	 *
	 * @param messageJSON the message json
	 * @param room the room
	 * @return the message
	 */
	public Message buildMessage(final JSONObject messageJSON, final Room room) {
		Message message = new Message();
		try {
			message.setId(messageJSON.getLong(JsonMessage.ID));
			message.setRoomId(messageJSON.getLong(JsonMessage.ROOM_ID));
			message.setUserId(!messageJSON.isNull(JsonMessage.USER_ID) ? messageJSON.getLong(JsonMessage.USER_ID) : 0L);
			message.setBody(messageJSON.getString(JsonMessage.BODY));
			message.setStarred(messageJSON.getBoolean(JsonMessage.STARRED));
			message.setType(messageJSON.getString(JsonMessage.TYPE));
			message.setCreatedAt(new SimpleDateFormat(API_TIME_FORMAT, 
			        Locale.US).parse(messageJSON.getString(JsonMessage.CREATED_AT)));
			message.setJSON(messageJSON);
		
			if (message.getType() == MessageType.UPLOAD) {
				message.setUpload(this.mApi.getMessageUpload(message.getRoomId(), message.getId()));
				Upload upload = message.getUpload();
				message.setBody(upload == null ? "deleted" : upload.getFullUrl());
			}
			
			if (message.getType() == MessageType.TWEET) {
				Object tweetObject = messageJSON.get(JsonTweet.OBJECT_NAME);

				// Campfire bug: Sometimes tweets are NULL
				if (tweetObject == JSONObject.NULL) {
					message.setType(MessageType.TEXT.toString());
				} else {
					message.setTweet(buildTweet(messageJSON.getJSONObject(JsonTweet.OBJECT_NAME)));
				}
			}
			
			if (room != null && message.getUserId() != 0) {
				
				User user = room.findUserById(message.getUserId());
				
				if (user == null) {
					message.setUser(this.mApi.getUser(message.getUserId()));
					room.addInactiveUser(message.getUser());
				} else {
					message.setUser(user);
				}
				
			}
			
		} catch (final JSONException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	/**
	 * Builds the uploads.
	 *
	 * @param httpResult the http result
	 * @return the list
	 */
	public List<Upload> buildUploads(final String httpResult) {
		List<Upload> uploads = new ArrayList<Upload>();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONArray uploadsJSON = json.getJSONArray(JsonUpload.OBJECT_NAME_PLURAL);
			
			for (int i = 0; i < uploadsJSON.length(); i = i + 1) {
				uploads.add(buildUpload(uploadsJSON.getJSONObject(i)));
			}
			
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return uploads;
	}
	
	/**
	 * Builds the upload.
	 *
	 * @param httpResult the http result
	 * @return the upload
	 */
	public Upload buildUpload(final String httpResult) {
		Upload upload = new Upload();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject uploadJSON = json.getJSONObject(JsonUpload.OBJECT_NAME);
			
			upload = this.buildUpload(uploadJSON);
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return upload;
	}
	
	/**
	 * Builds the upload.
	 *
	 * @param uploadJSON the upload json
	 * @return the upload
	 */
	public Upload buildUpload(final JSONObject uploadJSON) {
		Upload upload = new Upload();
		try {
			upload.setId(uploadJSON.getLong(JsonUpload.ID));
			upload.setRoomId(uploadJSON.getLong(JsonUpload.ROOM_ID));
			upload.setUserId(uploadJSON.getLong(JsonUpload.USER_ID));
			upload.setName(uploadJSON.getString(JsonUpload.NAME));
			upload.setByteSize(uploadJSON.getInt(JsonUpload.BYTE_SIZE));
			upload.setContentType(uploadJSON.getString(JsonUpload.CONTENT_TYPE));
			upload.setFullUrl(uploadJSON.getString(JsonUpload.FULL_URL));
			upload.setCreatedAt(new SimpleDateFormat(API_TIME_FORMAT, 
			        Locale.US).parse(uploadJSON.getString(JsonUpload.CREATED_AT)));
			upload.setJSON(uploadJSON);
		} catch (final JSONException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return upload;
	}
	
	/**
	 * Builds the user.
	 *
	 * @param httpResult the http result
	 * @return the user
	 */
	public User buildUser(final String httpResult) {
		User user = null;
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject userJSON = json.getJSONObject(JsonUser.OBJECT_NAME);
			
			user = this.buildUser(userJSON);
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Builds the user.
	 *
	 * @param userJSON the user json
	 * @return the user
	 */
	public User buildUser(final JSONObject userJSON) {
		User user = new User();
		try {
			user.setId(userJSON.getLong(JsonUser.ID));
			user.setName(userJSON.getString(JsonUser.NAME));
			user.setEmail(userJSON.getString(JsonUser.EMAIL_ADDRESS));
			user.setAdmin(userJSON.getBoolean(JsonUser.IS_ADMIN));
			user.setType(userJSON.getString(JsonUser.TYPE));
			user.setAvatarUrl(userJSON.getString(JsonUser.AVATAR_URL));
			user.setApiAuthToken(userJSON.has(JsonUser.API_AUTH_TOKEN) 
			        ? userJSON.getString(JsonUser.API_AUTH_TOKEN) : "");
			user.setCreatedAt(new SimpleDateFormat(API_TIME_FORMAT, 
			        Locale.US).parse(userJSON.getString(JsonUser.CREATED_AT)));
			user.setJSON(userJSON);
		} catch (final JSONException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Builds the tweet.
	 *
	 * @param tweetJSON the tweet json
	 * @return the tweet
	 */
	public Tweet buildTweet(final JSONObject tweetJSON) {
		Tweet tweet = new Tweet();
		try {
			tweet.setId(tweetJSON.getLong(JsonTweet.ID));
			tweet.setBody(tweetJSON.getString(JsonTweet.MESSAGE));
			tweet.setUsername(tweetJSON.getString(JsonTweet.AUTHOR_USERNAME));
			tweet.setProfileImageUrl(tweetJSON.getString(JsonTweet.AUTHOR_AVATAR_URL));
			tweet.setJSON(tweetJSON);
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return tweet;
	}

}
