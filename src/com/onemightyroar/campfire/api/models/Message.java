package com.onemightyroar.campfire.api.models;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.onemightyroar.campfire.api.utils.ToStringBuilder;


public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long roomId;
	private Long userId;
	private String body;
	private boolean starred;
	private MessageType type;
	private Date createdAt;
	private User user;
	private Upload upload;
	private Tweet tweet;
	private boolean isYoutubeLink = false;
	private boolean isImageLink = false;
	private boolean hasPlayed = true;
	private String youtubeThumbnailLink;
	private JSONObject json;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getRoomId() {
		return roomId;
	}
	
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
		this.isImageLink = parseImageLink();
		this.isYoutubeLink = parseYoutubeLink();
		
	}
	
	public boolean isStarred() {
		return starred;
	}
	
	public void setStarred(boolean starred) {
		this.starred = starred;
	}
	
	public MessageType getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = MessageType.fromString(type);
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Upload getUpload() {
		return upload;
	}
	
	public void setUpload(Upload upload) {
		this.upload = upload;
	}
	
	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}

	public Tweet getTweet() {
		return tweet;
	}
	
	public void setJSON(JSONObject json){
		this.json = json;
	}
	
	public JSONObject getJSON(){
		return json;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id",getId())
			.append("roomId",getRoomId())
			.append("userId",getUserId())
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
	 * @return 
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
	 * @return 
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
	 * @return 
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
	
	public boolean isYoutubeLink() {
		return this.isYoutubeLink;
	}
	
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
	 * @return
	 */
	public boolean hasPlayed() {
		return this.hasPlayed;
	}
	
	/**
	 * setPlayed
	 * 
	 * For sound messages, sets whether the sound is new or has been played.
	 * 
	 * @return
	 */
	public void setPlayed(boolean hasPlayed) {
		this.hasPlayed = hasPlayed;
	}
	
	public String getYoutubeLink() {
		return this.youtubeThumbnailLink;
	}
	
}
