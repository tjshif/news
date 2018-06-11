package com.school.Entity;

public class FocusDTO extends BaseDTO{
	private String fromUserID;
	private String toUserID;
	private Boolean bothStatus;
	private Boolean isCached;

	public FocusDTO()
	{

	}

	public FocusDTO(String fromUserID, String toUserID, Boolean bothStatus)
	{
		setFromUserID(fromUserID);
		setToUserID(toUserID);
		setBothStatus(bothStatus);
		setCached(false);
	}

	public void setFromUserID(String fromUserID) {
		this.fromUserID = fromUserID;
	}

	public void setToUserID(String toUserID) {
		this.toUserID = toUserID;
	}

	public void setBothStatus(Boolean bothStatus) {
		this.bothStatus = bothStatus;
	}

	public void setCached(Boolean cached) {
		isCached = cached;
	}

	public Boolean getBothStatus() {
		return bothStatus;
	}

	public String getFromUserID() {
		return fromUserID;
	}

	public String getToUserID() {
		return toUserID;
	}

	public Boolean getCached() {
		return isCached;
	}

	@Override
	public String getKey(String id)
	{
		return getItemKey(id);
	}

	public static String getItemKey(String id)
	{
		return String.format("Focus:%s", id);
	}
}
