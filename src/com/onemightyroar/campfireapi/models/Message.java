/**
 * Campfire Api - A Java library for Campfire from 37Signals
 *
 * @author		brianmuse
 * @copyright	2013 One Mighty Roar
 * @link		https://github.com/onemightyroar/campfire-api
 * @license		https://github.com/onemightyroar/campfire-api/blob/master/LICENSE.md
 * @version		1.0.0
 */

package com.onemightyroar.campfireapi.models;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;

// TODO: Auto-generated Javadoc
/**
 * Message
 * 
 * A campfire message. Has helper methods to handle special message types, like TweetMessage, youtube links, etc.
 * 
 * @author brianmuse
 * @see com.onemightyroar.campfireapi.MessageType
 * @see com.onemightyroar.campfireapi.models.Tweet
 * @see com.onemightyroar.campfireapi.models.Uplaod
 */
public class Message {
	
	/** The id. */
	private Long id;
	
	/** The room id. */
	private Long roomId;
	
	/** The user id. */
	private Long userId;
	
	/** The body. */
	private String body;
	
	/** The starred. */
	private boolean starred;
	
	/** The type. */
	private MessageType type;
	
	/** The created at. */
	private Date createdAt;
	
	/** The user. */
	private User user;
	
	/** The upload. */
	private Upload upload;
	
	/** The tweet. */
	private Tweet tweet;
	
	/** The is youtube link. */
	private boolean isYoutubeLink = false;
	
	/** The is image link. */
	private boolean isImageLink = false;
	
	/** The has played. */
	private boolean hasPlayed = true;
	
	/** The youtube thumbnail link. */
	private String youtubeThumbnailLink;
	
	/** The json. */
	private JSONObject json;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the room id.
	 *
	 * @return the room id
	 */
	public Long getRoomId() {
		return roomId;
	}
	
	/**
	 * Sets the room id.
	 *
	 * @param roomId the new room id
	 */
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	
	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * Sets the body.
	 *
	 * @param body the new body
	 */
	public void setBody(String body) {
		this.body = body;
		this.isImageLink = parseImageLink();
		this.isYoutubeLink = parseYoutubeLink();
		
	}
	
	/**
	 * Checks if is starred.
	 *
	 * @return true, if is starred
	 */
	public boolean isStarred() {
		return starred;
	}
	
	/**
	 * Sets the starred.
	 *
	 * @param starred the new starred
	 */
	public void setStarred(boolean starred) {
		this.starred = starred;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = MessageType.fromString(type);
	}
	
	/**
	 * Gets the created at.
	 *
	 * @return the created at
	 */
	public Date getCreatedAt() {
		return createdAt;
	}
	
	/**
	 * Sets the created at.
	 *
	 * @param createdAt the new created at
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the upload.
	 *
	 * @return the upload
	 */
	public Upload getUpload() {
		return upload;
	}
	
	/**
	 * Sets the upload.
	 *
	 * @param upload the new upload
	 */
	public void setUpload(Upload upload) {
		this.upload = upload;
	}
	
	/**
	 * Sets the tweet.
	 *
	 * @param tweet the new tweet
	 */
	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}

	/**
	 * Gets the tweet.
	 *
	 * @return the tweet
	 */
	public Tweet getTweet() {
		return tweet;
	}
	
	/**
	 * Sets the json.
	 *
	 * @param json the new json
	 */
	public void setJSON(JSONObject json){
		this.json = json;
	}
	
	/**
	 * Get the original JSON returned from the API.
	 *
	 * @return the json
	 */
	public JSONObject getJSON(){
		return json;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id",getId())
			.append("roomId",getRoomId())
			.append("userId",getUserId())
			.append("user",getUser())
			.append("body",getBody())
			.append("starred",isStarred())
			.append("type",getType().toString())
			.append("isYoutubeLink",isYoutubeLink())
			.append("isImageLink",isImageLink())
			.append("createdAt",getCreatedAt())
			.append("upload",(getUpload() == null) ? "" : getUpload().toString())
			.append("tweet",(getTweet() == null) ? "" : getTweet().toString())
			.toString();
	}
	
	/**
	 * parseImageLink
	 * 
	 * Returns true if the entirety of the message is a URL to an image.
	 *
	 * @return true, if successful
	 */
	private boolean parseImageLink() {
		Pattern pattern = Pattern.compile("^(http[^\\s]+(?:jpe?g|gif|png))(\\?[^\\s]*)?$");
		Matcher matcher = pattern.matcher(this.body);
		return matcher.matches();
	}
	
	/**
	 * parseYoutubeLink
	 * 
	 * Return true if the message body contains only a youtube link.
	 *
	 * @return true, if successful
	 */
	private boolean parseYoutubeLink() {
		String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

		Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = compiledPattern.matcher(this.body);
		while(matcher.find()) {
			String youtubeId = matcher.group(1);
			youtubeThumbnailLink = "http://i.ytimg.com/vi/" + youtubeId + "/hqdefault.jpg";
		    return true;
		}
		
		return false;
	}
	
	/**
	 * parseTwitterLink
	 * 
	 * Return true if the message body contains only a twitter status link.
	 *
	 * @return the integer
	 */
	public Integer parseTwitterLink() {
		String pattern = "https?:\\/\\/twitter\\.com\\/(?:#!\\/)?(\\w+)\\/status(es)?\\/(\\d+)*";

		Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = compiledPattern.matcher(this.body);
		while(matcher.find()) {
			return Integer.parseInt(matcher.group(4));
		}
		
		return 0;
	}
	
	/**
	 * Checks if is youtube link.
	 *
	 * @return true, if is youtube link
	 */
	public boolean isYoutubeLink() {
		return this.isYoutubeLink;
	}
	
	/**
	 * Checks if is image link.
	 *
	 * @return true, if is image link
	 */
	public boolean isImageLink() {
		return this.isImageLink;
	}
	
	/**
	 * hasPlayed
	 * 
	 * For sound messages, returns whether the sound is new or has been played.<br/>
	 * Use this with setPlayed.<br/><br/>
	 * 
	 * If the message comes through the message stream hasPlayed will be set to false.<br/>
	 * If the message comes through the "recent messages" API call, hasPlayed will be set to true.
	 *
	 * @return true, if successful
	 */
	public boolean hasPlayed() {
		return this.hasPlayed;
	}
	
	/**
	 * setPlayed
	 * 
	 * For sound messages, sets whether the sound is new or has been played.
	 *
	 * @param hasPlayed the new played
	 */
	public void setPlayed(boolean hasPlayed) {
		this.hasPlayed = hasPlayed;
	}
	
	/**
	 * Gets the youtube link.
	 *
	 * @return the youtube link
	 */
	public String getYoutubeLink() {
		return this.youtubeThumbnailLink;
	}
	
}
