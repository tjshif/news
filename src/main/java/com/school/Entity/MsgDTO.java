package com.school.Entity;

import java.sql.Timestamp;

public class MsgDTO extends BaseDTO {
	private Integer	newsType;
	private Integer	newsSubType;
	private Integer    locationCode;	//地点，可以更加学校所在地填写

	private Timestamp 	postDate;	//post的时间
	private Boolean 	isHot;
	private Boolean 	isValid = true;
	private Long 		publisherId;

	public Integer getNewsType() {
		return newsType;
	}

	public Integer getNewsSubType() {
		return newsSubType;
	}

	public void setNewsType(Integer newsType) {
		this.newsType = newsType;
	}

	public void setNewsSubType(Integer newsSubType) {
		this.newsSubType = newsSubType;
	}

	public void setLocationCode(Integer locationCode) {
		this.locationCode = locationCode;
	}

	public Integer getLocationCode() {
		return locationCode;
	}

	public void setPostDate(Timestamp postDate) {
		this.postDate = postDate;
	}

	public Timestamp getPostDate() {
		return postDate;
	}

	public Boolean getHot() {
		return isHot;
	}

	public Boolean getValid() {
		return isValid;
	}

	public void setHot(Boolean hot) {
		isHot = hot;
	}

	public void setValid(Boolean valid) {
		isValid = valid;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

}
