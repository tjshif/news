package com.school.Gson;

public class PostMsgGson {
	String content;
	Integer NewsType;
	Integer NewsSubType;
	Integer LocationCode;

	public String getContent() {
		return content;
	}

	public Integer getNewsType() {
		return NewsType;
	}

	public Integer getLocationCode() {
		return LocationCode;
	}

	public Integer getNewsSubType() {
		return NewsSubType;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setLocationCode(Integer locationCode) {
		LocationCode = locationCode;
	}

	public void setNewsSubType(Integer newsSubType) {
		NewsSubType = newsSubType;
	}

	public void setNewsType(Integer newsType) {
		NewsType = newsType;
	}
}
