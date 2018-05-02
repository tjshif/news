package com.school.Gson;

public class NewsCountResultGson extends RetResultGson {
	private Integer count;
	public NewsCountResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
