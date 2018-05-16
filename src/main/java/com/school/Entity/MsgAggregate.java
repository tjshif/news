package com.school.Entity;

public class MsgAggregate {
	private NewsDTO 		newsDTO;
	private NewsDetailDTO 	newsDetailDTO;

	public NewsDetailDTO getNewsDetailDTO() {
		return newsDetailDTO;
	}

	public void setNewsDetailDTO(NewsDetailDTO newsDetailDTO) {
		this.newsDetailDTO = newsDetailDTO;
	}

	public NewsDTO getNewsDTO() {
		return newsDTO;
	}

	public void setNewsDTO(NewsDTO newsDTO) {
		this.newsDTO = newsDTO;
	}
}
