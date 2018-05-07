package com.school.Entity;

public class ReplymeCommentDTO {
	private String ID;
	private String flID;
	private Long fromUserID;
	private String fromUserNickName;
	private String fromAvatarUrl;
	private String replyComment;
	private Long 	newsID;
	private String Subject;
	private Integer count;

	public void setFlID(String flID) {
		this.flID = flID;
	}

	public String getFlID() {
		return flID;
	}

	public void setFromAvatarUrl(String fromAvatarUrl) {
		this.fromAvatarUrl = fromAvatarUrl;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getFromUserNickName() {
		return fromUserNickName;
	}

	public String getFromAvatarUrl() {
		return fromAvatarUrl;
	}

	public void setFromUserID(Long fromUserID) {
		this.fromUserID = fromUserID;
	}

	public String getReplyComment() {
		return replyComment;
	}

	public void setFromUserNickName(String fromUserNickName) {
		this.fromUserNickName = fromUserNickName;
	}

	public void setReplyComment(String replyComment) {
		this.replyComment = replyComment;
	}

	public String getSubject() {
		return Subject;
	}

	public Long getFromUserID() {
		return fromUserID;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public void setNewsID(Long newsID) {
		this.newsID = newsID;
	}

	public Long getNewsID() {
		return newsID;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getID() {
		return ID;
	}
}
