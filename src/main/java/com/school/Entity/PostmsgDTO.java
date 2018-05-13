package com.school.Entity;

import java.sql.Timestamp;

public class PostmsgDTO extends BaseDTO{
	private String content;
	private String imagePaths;
	private Integer NewsType;
	private Integer NewsSubType;
	private Integer LocationCode;
	private Timestamp postDate;	//post的时间
	private Boolean isHot;
	private Boolean isValid;
	private Long publisherId;

	public void setNewsType(Integer newsType) {
		NewsType = newsType;
	}

	public void setNewsSubType(Integer newsSubType) {
		NewsSubType = newsSubType;
	}

	public void setLocationCode(Integer locationCode) {
		LocationCode = locationCode;
	}

	public Integer getNewsSubType() {
		return NewsSubType;
	}

	public Integer getLocationCode() {
		return LocationCode;
	}

	public Integer getNewsType() {
		return NewsType;
	}

	public void setPostDate(Timestamp postDate) {
		this.postDate = postDate;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getPostDate() {
		return postDate;
	}

	public String getContent() {
		return content;
	}

	public Boolean getHot() {
		return isHot;
	}

	public Boolean getValid() {
		return isValid;
	}

	public String getImagePaths() {
		return imagePaths;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setHot(Boolean hot) {
		isHot = hot;
	}

	public void setImagePaths(String imagePaths) {
		this.imagePaths = imagePaths;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public void setValid(Boolean valid) {
		isValid = valid;
	}
}
