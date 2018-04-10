package com.school.Gson;

import com.school.Entity.NewsDetailDTO;

public class NewsDetailResultGson extends RetResultGson{
	private NewsDetailDTO newsDetailDTO;

	public NewsDetailResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public void setNewsDetailDTO(NewsDetailDTO newsDetailDTO) {
		this.newsDetailDTO = newsDetailDTO;
	}

	public NewsDetailDTO getNewsDetailDTO() {
		return newsDetailDTO;
	}
}
