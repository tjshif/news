package com.school.Gson;

import org.apache.http.util.TextUtils;

import java.sql.Timestamp;

public class PostMsgGson {
	private String content;
	private Integer NewsType;
	private Integer NewsSubType;
	private Integer LocationCode;
	private Timestamp	postDate;
	private String		detailContent;
	private String		sourceArticleUrl;

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

	public Timestamp getPostDate() {
		return postDate;
	}

	public void setPostDate(Timestamp postDate) {
		this.postDate = postDate;
	}

	public String getDetailContent() {
		return detailContent;
	}

	public void setDetailContent(String detailContent) {
		this.detailContent = detailContent;
	}

	public String getSourceArticleUrl() {
		return sourceArticleUrl;
	}

	public void setSourceArticleUrl(String sourceArticleUrl) {
		this.sourceArticleUrl = sourceArticleUrl;
	}
	public Boolean getHasDetail()
	{
		if (TextUtils.isEmpty(detailContent))
			return false;
		else
			return true;
	}
}
