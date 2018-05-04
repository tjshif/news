package com.school.Entity;

public class SecondLevelCommentDTO extends BaseDTO {
	private Long flID;
	private Long fromUserID;
	private String fromUserNickName;
	private String fromAvatarUrl;

	private Long toUserID;
	private String toUserNickName;
	private String toAvatarUrl;
	private String replyComment;

	public SecondLevelCommentDTO()
	{

	}
	public SecondLevelCommentDTO(Long flID, Long fromUserID, String fromUserNickName, String fromAvatarUrl,
								 Long toUserID, String toUserNickName, String toAvatarUrl,
								 String replyComment)
	{
		setFlID(flID);
		setFromUserID(fromUserID);
		setFromUserNickName(fromUserNickName);
		setToUserID(toUserID);
		setToUserNickName(toUserNickName);
		setReplyComment(replyComment);
		setFromAvatarUrl(fromAvatarUrl);
		setToAvatarUrl(toAvatarUrl);
	}

	public String getFromAvatarUrl() {
		return fromAvatarUrl;
	}

	public void setFromAvatarUrl(String fromAvatarUrl) {
		this.fromAvatarUrl = fromAvatarUrl;
	}

	public String getToAvatarUrl() {
		return toAvatarUrl;
	}

	public void setToAvatarUrl(String toAvatarUrl) {
		this.toAvatarUrl = toAvatarUrl;
	}

	public Long getFlID() {
		return flID;
	}

	public Long getFromUserID() {
		return fromUserID;
	}

	public void setToUserNickName(String toUserNickName) {
		this.toUserNickName = toUserNickName;
	}

	public void setToUserID(Long toUserID) {
		this.toUserID = toUserID;
	}

	public void setReplyComment(String replyComment) {
		this.replyComment = replyComment;
	}

	public void setFromUserNickName(String fromUserNickName) {
		this.fromUserNickName = fromUserNickName;
	}

	public void setFromUserID(Long fromUserID) {
		this.fromUserID = fromUserID;
	}

	public void setFlID(Long flID) {
		this.flID = flID;
	}

	public String getToUserNickName() {
		return toUserNickName;
	}

	public String getReplyComment() {
		return replyComment;
	}

	public String getFromUserNickName() {
		return fromUserNickName;
	}

	public Long getToUserID() {
		return toUserID;
	}
}
