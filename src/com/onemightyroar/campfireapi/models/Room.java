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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;

// TODO: Auto-generated Javadoc
/**
 * Room
 * 
 * A Campfire room.
 *
 * @author brianmuse
 */
public class Room {
	
	/** The id. */
	private Long id;
	
	/** The name. */
	private String name;
	
	/** The membership limit. */
	private Integer membershipLimit;
	
	/** The topic. */
	private String topic;
	
	/** The open to guests. */
	private boolean openToGuests;
	
	/** The full. */
	private boolean full;
	
	/** The present. */
	private boolean present;
	
	/** The active token value. */
	private String activeTokenValue;
	
	/** The active users. */
	private Map<Long, User> activeUsers = new HashMap<Long, User>();
	
	/** The inactive users. */
	private Map<Long, User> inactiveUsers = new HashMap<Long, User>();
	
	/** The created at. */
	private Date createdAt;
	
	/** The updated at. */
	private Date updatedAt;
	
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
	 * Gets the membership limit.
	 *
	 * @return the membership limit
	 */
	public Integer getMembershipLimit() {
		return membershipLimit;
	}
	
	/**
	 * Sets the membership limit.
	 *
	 * @param membershipLimit the new membership limit
	 */
	public void setMembershipLimit(Integer membershipLimit) {
		this.membershipLimit = membershipLimit;
	}
	
	/**
	 * Gets the topic.
	 *
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}
	
	/**
	 * Sets the topic.
	 *
	 * @param topic the new topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	/**
	 * Checks if is open to guests.
	 *
	 * @return true, if is open to guests
	 */
	public boolean isOpenToGuests() {
		return openToGuests;
	}
	
	/**
	 * Sets the open to guests.
	 *
	 * @param openToGuests the new open to guests
	 */
	public void setOpenToGuests(boolean openToGuests) {
		this.openToGuests = openToGuests;
	}
	
	/**
	 * Checks if is full.
	 *
	 * @return true, if is full
	 */
	public boolean isFull() {
		return full;
	}
	
	/**
	 * Sets the full.
	 *
	 * @param full the new full
	 */
	public void setFull(boolean full) {
		this.full = full;
	}
	
	/**
	 * Gets the active token value.
	 *
	 * @return the active token value
	 */
	public String getActiveTokenValue() {
		return activeTokenValue;
	}
	
	/**
	 * Sets the active token value.
	 *
	 * @param activeTokenValue the new active token value
	 */
	public void setActiveTokenValue(String activeTokenValue) {
		this.activeTokenValue = activeTokenValue;
	}
	
	/**
	 * Gets the active users.
	 *
	 * @return the active users
	 */
	public Map<Long, User> getActiveUsers() {
		return activeUsers;
	}
	
	/**
	 * Gets the active user.
	 *
	 * @param userId the user id
	 * @return the active user
	 */
	public User getActiveUser(Long userId) {
		return activeUsers.get(userId);
	}
	
	/**
	 * Sets the active users.
	 *
	 * @param activeUsers the new active users
	 */
	public void setActiveUsers(List<User> activeUsers) {
		for(User user : activeUsers)
		this.activeUsers.put(user.getId(), user);
	}
	
	/**
	 * Adds the active user.
	 *
	 * @param user the user
	 */
	public void addActiveUser(User user) {
		this.activeUsers.put(user.getId(), user);
		this.inactiveUsers.remove(user.getId());
	}
	
	/**
	 * Gets the inactive users.
	 *
	 * @return the inactive users
	 */
	public Map<Long, User> getInactiveUsers() {
		return inactiveUsers;
	}
	
	/**
	 * Gets the inactive user.
	 *
	 * @param userId the user id
	 * @return the inactive user
	 */
	public User getInactiveUser(Long userId) {
		return inactiveUsers.get(userId);
	}
	
	/**
	 * Adds the inactive user.
	 *
	 * @param user the user
	 */
	public void addInactiveUser(User user) {
		this.activeUsers.remove(user.getId());
		this.inactiveUsers.put(user.getId(), user);
	}
	
	/**
	 * Sets the presence.
	 *
	 * @param present the new presence
	 */
	public void setPresence(boolean present) {
		this.present = present;
	}

	/**
	 * Checks if is present.
	 *
	 * @return true, if is present
	 */
	public boolean isPresent() {
		return present;
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
	 * Gets the updated at.
	 *
	 * @return the updated at
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	/**
	 * Sets the updated at.
	 *
	 * @param updatedAt the new updated at
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	/**
	 * Find user by id.
	 *
	 * @param id the id
	 * @return the user
	 */
	public User findUserById(Long id) {
		User user;
		
		user = this.getActiveUser(id);
		if(user != null) return user;
		
		user = this.getInactiveUser(id);
		if(user != null) return user;
		
		return null;
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
