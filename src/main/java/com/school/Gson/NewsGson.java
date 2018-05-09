package com.school.Gson;

import java.sql.Timestamp;

public class NewsGson {
	private Long ID;
	private String 	subject;
	private Integer	newsType;
	private Integer	newsSubType;
	private Integer    locationCode;
	private Timestamp  updateAt;
	private String		updateBy = "sys";

	public void setID(Long ID) {
		this.ID = ID;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public Long getID() {
		return ID;
	}

	public Integer getNewsType() {
		return newsType;
	}

	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getLocationCode() {
		return locationCode;
	}

	public Integer getNewsSubType() {
		return newsSubType;
	}

	public void setLocationCode(Integer locationCode) {
		this.locationCode = locationCode;
	}

	public void setNewsSubType(Integer newsSubType) {
		this.newsSubType = newsSubType;
	}

	public void setNewsType(Integer newsType) {
		this.newsType = newsType;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Timestamp getUpdateAt() {
		return updateAt;
	}

	public String getUpdateBy() {
		return updateBy;
	}
}
