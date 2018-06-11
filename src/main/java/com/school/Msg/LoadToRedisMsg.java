package com.school.Msg;

public class LoadToRedisMsg {
	private Integer type;
	private String id;

	public LoadToRedisMsg(Integer type, String id)
	{
		setId(id);
		setType(type);
	}

	public Integer getType() {
		return type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
