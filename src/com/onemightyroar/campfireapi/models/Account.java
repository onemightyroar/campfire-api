package com.onemightyroar.campfireapi.models;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;


public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String subdomain;
	private String plan;
	private Long ownerId;
	private String timezone;
	private Integer storage;
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
	
	public String getSubdomain() {
		return subdomain;
	}
	
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}
	
	public String getPlan() {
		return plan;
	}
	
	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getTimezone() {
		return timezone;
	}
	
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	public Integer getStorage() {
		return storage;
	}
	
	public void setStorage(Integer storage) {
		this.storage = storage;
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
