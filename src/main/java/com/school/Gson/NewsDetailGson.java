package com.school.Gson;

import java.sql.Timestamp;

public class NewsDetailGson {
	private Long newsID;
	private String detailContent;
	private Timestamp updateAt;
	private String		updateBy = "sys";

	public Long getNewsID() {
		return newsID;
	}

	public void setNewsID(Long newsID) {
		this.newsID = newsID;
	}

	public void setDetailContent(String detailContent) {
		this.detailContent = detailContent;
	}

	public String getDetailContent() {
		return detailContent;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public Timestamp getUpdateAt() {
		return updateAt;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}
}
