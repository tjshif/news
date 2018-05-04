package com.school.Entity;

import java.util.List;

public class FirstLevelCommentDTO extends BaseDTO {
	private Long 	newsID;
	private String Subject;
	private Long	userID;
	private String nickName;
	private String avatarUrl;
	private String comment;
	private Integer count;
	private List<SecondLevelCommentDTO> secondLevelComments;

	public FirstLevelCommentDTO()
	{

	}
	public FirstLevelCommentDTO(Long newsID, String subject, Long userID, String nickName, String avatarUrl, String comment)
	{
		setNewsID(newsID);
		setSubject(subject);
		setUserID(userID);
		setNickName(nickName);
		setAvatarUrl(avatarUrl);
		setComment(comment);
		setCount(1);
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getSubject() {
		return Subject;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public void setNewsID(Long newsID) {
		this.newsID = newsID;
	}

	public Long getUserID() {
		return userID;
	}

	public Long getNewsID() {
		return newsID;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setSecondLevelComments(List<SecondLevelCommentDTO> secondLevelComments) {
		this.secondLevelComments = secondLevelComments;
	}

	public List<SecondLevelCommentDTO> getSecondLevelComments() {
		return secondLevelComments;
	}
}
