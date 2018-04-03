package com.school.Gson;

import com.school.Entity.NewsDTO;

import java.util.List;

public class NewsSubjectResultGson extends RetResultGson{
	private List<NewsDTO> newsList;

	public NewsSubjectResultGson(int retCode, String message)
	{
		super(retCode, message);
	}
	public void setNewsList(List<NewsDTO> newsList) {
		this.newsList = newsList;
	}

	public List<NewsDTO> getNewsList() {
		return newsList;
	}
}
