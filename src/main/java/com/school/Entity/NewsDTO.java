package com.school.Entity;

import java.sql.Timestamp;

public class NewsDTO extends MsgDTO {
	private String 	subject;
	private String     linkUrl;
	private String     publishSource;
	private Integer	commentCount;

	public void setPublishSource(String publishSource) {
		this.publishSource = publishSource;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public String getSubject() {
		return subject;
	}
}
