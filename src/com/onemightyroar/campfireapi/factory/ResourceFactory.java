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

public class ResourceFactory {
	
	private CampfireApi mApi;
	
	public ResourceFactory(CampfireApi api) {
		mApi = api;
	}
	
	public Account buildAccount(String httpResult) {
		Account account = new Account();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject accountJSON = json.getJSONObject("account");
			account.setId(accountJSON.getLong("id"));
			account.setName(accountJSON.getString("name"));
			account.setSubdomain(accountJSON.getString("subdomain"));
			account.setPlan(accountJSON.getString("plan"));
			account.setOwnerId(accountJSON.getLong("owner_id"));
			account.setTimezone(accountJSON.getString("time_zone"));
			account.setStorage(accountJSON.getInt("storage"));
			account.setUpdatedAt(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.US).parse(accountJSON.getString("updated_at")));
			account.setCreatedAt(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.US).parse(accountJSON.getString("created_at")));
			account.setJSON(accountJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return account;
	}
	
	public List<Room> buildRooms(String httpResult) {
		List<Room> rooms = new ArrayList<Room>();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONArray roomsJSON = json.getJSONArray("rooms");
			
			for(int i = 0; i < roomsJSON.length(); i++) {
				rooms.add(buildRoom(roomsJSON.getJSONObject(i)));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rooms;
	}
	
	public Room buildRoom(String httpResult) {
		Room room = new Room();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject roomJSON = json.getJSONObject("room");
			
			room = this.buildRoom(roomJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return room;
	}
	
	public Room buildRoom(JSONObject roomJSON) {
		Room room = new Room();
		
		try {
			room.setId(roomJSON.getLong("id"));
			room.setName(roomJSON.getString("name"));
			room.setMembershipLimit(roomJSON.getInt("membership_limit"));
			room.setTopic(roomJSON.getString("topic"));
			room.setOpenToGuests(roomJSON.has("open_to_guests") ? roomJSON.getBoolean("open_to_guests") : false);
			room.setFull(roomJSON.has("full") ? roomJSON.getBoolean("full") : false);
			room.setActiveTokenValue(roomJSON.has("active_token_value") ? roomJSON.getString("active_token_value") : "");
			room.setJSON(roomJSON);
			
			if(roomJSON.has("users")) {
				JSONArray usersJSON = roomJSON.getJSONArray("users");
				for(int i = 0; i < usersJSON.length(); i++) {
					room.addActiveUser(buildUser(usersJSON.getJSONObject(i)));
				}
			}

			room.setUpdatedAt(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.US).parse(roomJSON.getString("updated_at")));
			room.setCreatedAt(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.US).parse(roomJSON.getString("created_at")));
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return room;
	}
	
	public List<Message> buildMessages(String httpResult) {
		return buildMessages(httpResult, null);
	}
	
	public List<Message> buildMessages(String httpResult, Room room) {
		List<Message> messages = new ArrayList<Message>();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONArray messagesJSON = json.getJSONArray("messages");
			
			for(int i = 0; i < messagesJSON.length(); i++) {
				messages.add(buildMessage(messagesJSON.getJSONObject(i), room));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	public Message buildMessage(String httpResult) {
		return buildMessage(httpResult, null);
	}
	
	public Message buildMessage(String httpResult, Room room) {
		Message message = new Message();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject messageJSON = json.getJSONObject("message");
			
			message = this.buildMessage(messageJSON, room);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public Message buildMessage(JSONObject messageJSON) {
		return buildMessage(messageJSON, null);
	}
	
	public Message buildMessage(JSONObject messageJSON, Room room) {
		Message message = new Message();
		try {
			message.setId(messageJSON.getLong("id"));
			message.setRoomId(messageJSON.getLong("room_id"));
			message.setUserId(!messageJSON.isNull("user_id") ? messageJSON.getLong("user_id") : 0L);
			message.setBody(messageJSON.getString("body"));
			message.setStarred(messageJSON.getBoolean("starred"));
			message.setType(messageJSON.getString("type"));
			message.setCreatedAt(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.US).parse(messageJSON.getString("created_at")));
			message.setJSON(messageJSON);
		
			if(message.getType() == MessageType.UPLOAD) {
				message.setUpload(mApi.getMessageUpload(message.getRoomId(), message.getId()));
				Upload upload = message.getUpload();
				message.setBody(upload == null ? "deleted" : upload.getFullUrl());
			}
			
			if(message.getType() == MessageType.TWEET) {
				Object tweetObject = messageJSON.get("tweet");

				// Campfire bug: Sometimes tweets are NULL
				if(tweetObject == JSONObject.NULL)
				{
					message.setType("TextMessage");
				} 
				// Everything went fine.
				else {
					message.setTweet(buildTweet(messageJSON.getJSONObject("tweet")));
				}
			}
			
			if(room != null && message.getUserId() != 0) {
				
				User user = room.findUserById(message.getUserId());
				
				if(user == null) {
					message.setUser(mApi.getUser(message.getUserId()));
					room.addInactiveUser(message.getUser());
				}else{
					message.setUser(user);
				}
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public List<Upload> buildUploads(String httpResult) {
		List<Upload> uploads = new ArrayList<Upload>();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONArray uploadsJSON = json.getJSONArray("uploads");
			
			for(int i = 0; i < uploadsJSON.length(); i++) {
				uploads.add(buildUpload(uploadsJSON.getJSONObject(i)));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return uploads;
	}
	
	public Upload buildUpload(String httpResult) {
		Upload upload = new Upload();
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject uploadJSON = json.getJSONObject("upload");
			
			upload = this.buildUpload(uploadJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return upload;
	}
	
	public Upload buildUpload(JSONObject uploadJSON) {
		Upload upload = new Upload();
		try {
			upload.setId(uploadJSON.getLong("id"));
			upload.setRoomId(uploadJSON.getLong("room_id"));
			upload.setUserId(uploadJSON.getLong("user_id"));
			upload.setName(uploadJSON.getString("name"));
			upload.setByteSize(uploadJSON.getInt("byte_size"));
			upload.setContentType(uploadJSON.getString("content_type"));
			upload.setFullUrl(uploadJSON.getString("full_url"));
			upload.setCreatedAt(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.US).parse(uploadJSON.getString("created_at")));
			upload.setJSON(uploadJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return upload;
	}
	
	public User buildUser(String httpResult) {
		User user = null;
		try {
			JSONObject json = new JSONObject(httpResult);
			JSONObject userJSON = json.getJSONObject("user");
			
			user = this.buildUser(userJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	public User buildUser(JSONObject userJSON) {
		User user = new User();
		try {
			user.setId(userJSON.getLong("id"));
			user.setName(userJSON.getString("name"));
			user.setEmail(userJSON.getString("email_address"));
			user.setAdmin(userJSON.getBoolean("admin"));
			user.setType(userJSON.getString("type"));
			user.setAvatarUrl(userJSON.getString("avatar_url"));
			user.setApiAuthToken(userJSON.has("api_auth_token") ? userJSON.getString("api_auth_token") : "");
			user.setCreatedAt(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.US).parse(userJSON.getString("created_at")));
			user.setJSON(userJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return user;
	}

	public Tweet buildTweet(JSONObject tweetJSON) {
		Tweet tweet = new Tweet();
		try {
			tweet.setId(tweetJSON.getLong("id"));
			tweet.setBody(tweetJSON.getString("message"));
			tweet.setUsername(tweetJSON.getString("author_username"));
			tweet.setProfileImageUrl(tweetJSON.getString("author_avatar_url"));
			tweet.setJSON(tweetJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tweet;
	}

}
