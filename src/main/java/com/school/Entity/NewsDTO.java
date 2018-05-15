package com.school.Entity;

import java.sql.Timestamp;

public class NewsDTO extends MsgDTO {
	private String 	subject;
	private String     linkUrl;
	private Integer	commentCount;


	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public String getContent() {
		return subject;
	}
}
