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

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;

/**
 * Tweet
 * 
 * A tweet object attached to a {@link com.onemightyroar.campfireapi.models.MessageType TweetMessage}
 * 
 * @author brianmuse
 */
public class Tweet {
	
	/** The id. */
	private Long mId;
	
	/** The body. */
	private String mBody;
	
	/** The username. */
	private String mUsername;
	
	/** The profile image url. */
	private String mProfileImageUrl;
	
	/** The json. */
	private JSONObject mJson;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return this.mId;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(final Long id) {
		this.mId = id;
	}
	
	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public String getBody() {
		return this.mBody;
	}
	
	/**
	 * Sets the body.
	 *
	 * @param body the new body
	 */
	public void setBody(final String body) {
		this.mBody = body;
	}
	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return this.mUsername;
	}
	
	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(final String username) {
		this.mUsername = username;
	}
	
	/**
	 * Gets the profile image url.
	 *
	 * @return the profile image url
	 */
	public String getProfileImageUrl() {
		return this.mProfileImageUrl;
	}
	
	/**
	 * Sets the profile image url.
	 *
	 * @param profileImageUrl the new profile image url
	 */
	public void setProfileImageUrl(final String profileImageUrl) {
		this.mProfileImageUrl = profileImageUrl;
	}
	
	/**
	 * Sets the json.
	 *
	 * @param json the new json
	 */
	public void setJSON(final JSONObject json) {
		this.mJson = json;
	}
	
	/**
	 * Gets the json.
	 *
	 * @return the json
	 */
	public JSONObject getJSON() {
		return this.mJson;
	}
	
	/**
	 * @return The object in string form
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", this.getId())
			.append("body", this.getBody())
			.append("username", this.getUsername())
			.append("profileImageUrl", this.getProfileImageUrl())
			.toString();
	}
	
}
