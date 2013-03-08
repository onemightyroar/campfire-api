package com.onemightyroar.campfireapi.models;

import java.util.Date;

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;


// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
public class User {

	/** The id. */
	private Long id;
	
	/** The name. */
	private String name;
	
	/** The email. */
	private String email;
	
	/** The admin. */
	private boolean admin;
	
	/** The type. */
	private String type;
	
	/** The avatar url. */
	private String avatarUrl;
	
	/** The api auth token. */
	private String apiAuthToken;
	
	/** The created at. */
	private Date createdAt;
	
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Checks if is admin.
	 *
	 * @return true, if is admin
	 */
	public boolean isAdmin() {
		return admin;
	}
	
	/**
	 * Sets the admin.
	 *
	 * @param admin the new admin
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param topic the new type
	 */
	public void setType(String topic) {
		this.type = topic;
	}
	
	/**
	 * Gets the avatar url.
	 *
	 * @return the avatar url
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}
	
	/**
	 * Sets the avatar url.
	 *
	 * @param avatarUrl the new avatar url
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
	/**
	 * Gets the api auth token.
	 *
	 * @return the api auth token
	 */
	public String getApiAuthToken() {
		return apiAuthToken;
	}
	
	/**
	 * Sets the api auth token.
	 *
	 * @param apiAuthToken the new api auth token
	 */
	public void setApiAuthToken(String apiAuthToken) {
		this.apiAuthToken = apiAuthToken;
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
	 * Sets the json.
	 *
	 * @param json the new json
	 */
	public void setJSON(JSONObject json){
		this.json = json;
	}
	
	/**
	 * Gets the json.
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
			.append("name",getName())
			.append("email",getEmail())
			.append("admin",isAdmin())
			.append("type",getType())
			.append("avatarUrl",getAvatarUrl())
			.append("apiAuthToken",getApiAuthToken())
			.append("createdAt",getCreatedAt())
			.toString();
	}
	
}
