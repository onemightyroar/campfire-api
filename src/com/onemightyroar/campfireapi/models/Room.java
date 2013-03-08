package com.onemightyroar.campfireapi.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;


public class Room implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private Integer membershipLimit;
	private String topic;
	private boolean openToGuests;
	private boolean full;
	private boolean present;
	private String activeTokenValue;
	private Map<Long, User> activeUsers = new HashMap<Long, User>();
	private Map<Long, User> inactiveUsers = new HashMap<Long, User>();
	private Date createdAt;
	private Date updatedAt;
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
	
	public Integer getMembershipLimit() {
		return membershipLimit;
	}
	
	public void setMembershipLimit(Integer membershipLimit) {
		this.membershipLimit = membershipLimit;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public boolean isOpenToGuests() {
		return openToGuests;
	}
	
	public void setOpenToGuests(boolean openToGuests) {
		this.openToGuests = openToGuests;
	}
	
	public boolean isFull() {
		return full;
	}
	
	public void setFull(boolean full) {
		this.full = full;
	}
	
	public String getActiveTokenValue() {
		return activeTokenValue;
	}
	
	public void setActiveTokenValue(String activeTokenValue) {
		this.activeTokenValue = activeTokenValue;
	}
	
	public Map<Long, User> getActiveUsers() {
		return activeUsers;
	}
	
	public User getActiveUser(Long userId) {
		return activeUsers.get(userId);
	}
	
	public void setActiveUsers(List<User> activeUsers) {
		for(User user : activeUsers)
		this.activeUsers.put(user.getId(), user);
	}
	
	public void addActiveUser(User user) {
		this.activeUsers.put(user.getId(), user);
		this.inactiveUsers.remove(user.getId());
	}
	
	public Map<Long, User> getInactiveUsers() {
		return inactiveUsers;
	}
	
	public User getInactiveUser(Long userId) {
		return inactiveUsers.get(userId);
	}
	
	public void addInactiveUser(User user) {
		this.activeUsers.remove(user.getId());
		this.inactiveUsers.put(user.getId(), user);
	}
	
	public void setPresence(boolean present) {
		this.present = present;
	}

	public boolean isPresent() {
		return present;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public User findUserById(Long id) {
		User user;
		
		user = this.getActiveUser(id);
		if(user != null) return user;
		
		user = this.getInactiveUser(id);
		if(user != null) return user;
		
		return null;
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
			.append("membershipLimit",getMembershipLimit())
			.append("topic",getTopic())
			.append("openToGuests",isOpenToGuests())
			.append("full",isFull())
			.append("activeTokenValue",getActiveTokenValue())
			.append("activeUsers",getActiveUsers().toString())
			.append("createdAt",getCreatedAt())
			.append("updatedAt",getUpdatedAt())
			.toString();
	}
	
}
