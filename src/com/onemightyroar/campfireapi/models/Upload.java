package com.onemightyroar.campfireapi.models;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.onemightyroar.campfireapi.utils.ToStringBuilder;


// TODO: Auto-generated Javadoc
/**
 * The Class Upload.
 */
public class Upload {
	
	/** The id. */
	private Long id;
	
	/** The room id. */
	private Long roomId;
	
	/** The user id. */
	private Long userId;
	
	/** The name. */
	private String name;
	
	/** The byte size. */
	private Integer byteSize;
	
	/** The content type. */
	private String contentType;
	
	/** The full url. */
	private String fullUrl;
	
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
	 * Gets the room id.
	 *
	 * @return the room id
	 */
	public Long getRoomId() {
		return roomId;
	}
	
	/**
	 * Sets the room id.
	 *
	 * @param roomId the new room id
	 */
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	
	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
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
	 * Gets the byte size.
	 *
	 * @return the byte size
	 */
	public Integer getByteSize() {
		return byteSize;
	}
	
	/**
	 * Sets the byte size.
	 *
	 * @param byteSize the new byte size
	 */
	public void setByteSize(Integer byteSize) {
		this.byteSize = byteSize;
	}
	
	/**
	 * Gets the content type.
	 *
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * Sets the content type.
	 *
	 * @param contentType the new content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * Gets the full url.
	 *
	 * @return the full url
	 */
	public String getFullUrl() {
		return fullUrl;
	}
	
	/**
	 * Sets the full url.
	 *
	 * @param fullUrl the new full url
	 */
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
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
	 * Returns true if the upload is an image.
	 *
	 * @return true, if is image
	 */
	public boolean isImage() {
		Pattern pattern = Pattern.compile("^(http[^\\s]+(?:jpe?g|gif|png))(\\?[^\\s]*)?$");
		Matcher matcher = pattern.matcher(this.getFullUrl());
		return matcher.matches();
	}
	
	/**
	 * isPdf
	 * 
	 * Returns true if the upload is a pdf.
	 *
	 * @return true, if is pdf
	 */
	public boolean isPdf() {
		Pattern pattern = Pattern.compile("^(http[^\\s]+(?:pdf))(\\?[^\\s]*)?$");
		Matcher matcher = pattern.matcher(this.getFullUrl());
		return matcher.matches();
	}
	
}
