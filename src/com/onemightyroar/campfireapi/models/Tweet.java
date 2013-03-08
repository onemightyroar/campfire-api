package com.onemightyroar.campfireapi.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;


public class Tweet implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String body;
	private String username;
	private String profileImageUrl;
	private JSONObject json;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
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
			.append("body",getBody())
			.append("username",getUsername())
			.append("profileImageUrl",getProfileImageUrl())
			.toString();
	}
	
}
