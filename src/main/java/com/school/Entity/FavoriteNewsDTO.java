package com.school.Entity;

import java.sql.Timestamp;

public class FavoriteNewsDTO {
	private Long ID;
	private Long userID;
	private Long newsID;
	private Timestamp createAt;

	public FavoriteNewsDTO()
	{

	}
	public FavoriteNewsDTO(Long userID, Long newsID)
	{
		this.userID = userID;
		this.newsID = newsID;
	}

	public Long getNewsID() {
		return newsID;
	}

	public Long getUserID() {
		return userID;
	}

	public Timestamp getCreateAt() {
		return createAt;
	}
}
