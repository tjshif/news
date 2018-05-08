package com.school.Gson;

public class RetBeAdminLoginGson extends RetResultGson {
	private String ID;
	private String sessionID;
	public RetBeAdminLoginGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}
}
