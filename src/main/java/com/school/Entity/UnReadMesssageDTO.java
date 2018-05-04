package com.school.Entity;

public class UnReadMesssageDTO extends BaseDTO{
	private Long userID;
	private Integer unReadCount;

	public UnReadMesssageDTO(Long userID)
	{
		setUserID(userID);
		setUnReadCount(1);
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Long getUserID() {
		return userID;
	}

	public Integer getUnReadCount() {
		return unReadCount;
	}

	public void setUnReadCount(Integer unReadCount) {
		this.unReadCount = unReadCount;
	}
}
