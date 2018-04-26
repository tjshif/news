package com.school.Gson;

public class RetIDResultGson extends RetResultGson {
	private Long ID;

	public RetIDResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public void setID(Long ID) {
		this.ID = ID;
	}

	public Long getID() {
		return ID;
	}
}
