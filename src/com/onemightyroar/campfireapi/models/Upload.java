package com.onemightyroar.campfireapi.models;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;


public class Upload {
	
	private Long id;
	private Long roomId;
	private Long userId;
	private String name;
	private Integer byteSize;
	private String contentType;
	private String fullUrl;
	private Date createdAt;
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getByteSize() {
		return byteSize;
	}
	
	public void setByteSize(Integer byteSize) {
		this.byteSize = byteSize;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getFullUrl() {
		return fullUrl;
	}
	
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
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
			.append("roomId",getRoomId())
			.append("userId",getUserId())
			.append("name",getName())
			.append("byteSize",getByteSize())
			.append("contentType",getContentType())
			.append("fullUrl",getFullUrl())
			.append("createdAt",getCreatedAt())
			.toString();
	}
	
	/**
	 * isImageFile
	 * 
	 * Returns true if the upload is an image
	 * 
	 * @param messageBody
	 * @return 
	 */
	public boolean isImage() {
		Pattern pattern = Pattern.compile("^(http[^\\s]+(?:jpe?g|gif|png))(\\?[^\\s]*)?$");
		Matcher matcher = pattern.matcher(this.getFullUrl());
		return matcher.matches();
	}
	
	/**
	 * isPdf
	 * 
	 * Returns true if the upload is a pdf
	 * 
	 * @param messageBody
	 * @return 
	 */
	public boolean isPdf() {
		Pattern pattern = Pattern.compile("^(http[^\\s]+(?:pdf))(\\?[^\\s]*)?$");
		Matcher matcher = pattern.matcher(this.getFullUrl());
		return matcher.matches();
	}
	
}
