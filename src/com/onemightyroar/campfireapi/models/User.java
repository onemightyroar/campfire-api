package com.onemightyroar.campfireapi.models;

import java.util.Date;

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;


public class User {

	private Long id;
	private String name;
	private String email;
	private boolean admin;
	private String type;
	private String avatarUrl;
	private String apiAuthToken;
	private Date createdAt;
	private JSONObject json;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String topic) {
		this.type = topic;
	}
	
	public String getAvatarUrl() {
		return avatarUrl;
	}
	
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
	public String getApiAuthToken() {
		return apiAuthToken;
	}
	
	public void setApiAuthToken(String apiAuthToken) {
		this.apiAuthToken = apiAuthToken;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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
