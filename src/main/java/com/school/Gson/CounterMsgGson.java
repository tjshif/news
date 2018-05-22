package com.school.Gson;

public class CounterMsgGson {
	private String newsID;

	public CounterMsgGson(String id)
	{
		setNewsID(id);
	}

	public void setNewsID(String newsID) {
		this.newsID = newsID;
	}

	public String getNewsID() {
		return newsID;
	}
}
