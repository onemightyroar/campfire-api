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

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;

// TODO: Auto-generated Javadoc
/**
 * Account
 * 
 * A campfire account.
 *
 * @author brianmuse
 */
public class Account {
	
	/** The id. */
	private Long id;
	
	/** The name. */
	private String name;
	
	/** The subdomain. */
	private String subdomain;
	
	/** The plan. */
	private String plan;
	
	/** The owner id. */
	private Long ownerId;
	
	/** The timezone. */
	private String timezone;
	
	/** The storage. */
	private Integer storage;
	
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
	 * Gets the subdomain.
	 *
	 * @return the subdomain
	 */
	public String getSubdomain() {
		return subdomain;
	}
	
	/**
	 * Sets the subdomain.
	 *
	 * @param subdomain the new subdomain
	 */
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}
	
	/**
	 * Gets the plan.
	 *
	 * @return the plan
	 */
	public String getPlan() {
		return plan;
	}
	
	/**
	 * Sets the plan.
	 *
	 * @param plan the new plan
	 */
	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	/**
	 * Gets the owner id.
	 *
	 * @return the owner id
	 */
	public Long getOwnerId() {
		return ownerId;
	}
	
	/**
	 * Sets the owner id.
	 *
	 * @param ownerId the new owner id
	 */
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	/**
	 * Gets the timezone.
	 *
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}
	
	/**
	 * Sets the timezone.
	 *
	 * @param timezone the new timezone
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	/**
	 * Gets the storage.
	 *
	 * @return the storage
	 */
	public Integer getStorage() {
		return storage;
	}
	
	/**
	 * Sets the storage.
	 *
	 * @param storage the new storage
	 */
	public void setStorage(Integer storage) {
		this.storage = storage;
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
			.append("ownerId",getOwnerId())
			.append("planId",getPlan())
			.append("name",getName())
			.append("thirdLevelDomain",getSubdomain())
			.append("timeZone",getTimezone())
			.append("suspended",getStorage())
			.append("createdAt",getCreatedAt())
			.append("updatedAt",getUpdatedAt())
			.toString();
	}
	
}
