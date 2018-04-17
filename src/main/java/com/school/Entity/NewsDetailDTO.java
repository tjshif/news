package com.school.Entity;

import java.sql.Timestamp;

public class NewsDetailDTO {
	private Long newsID;
	private String Subject;
	private String detailContent;
	private Boolean favorite;
	private Timestamp postDate;	//post的时间
	private Long    publisher_id;
	private String    publisher_avatar_url;
	private String    publisher_name;

	public Long getNewsID() {
		return newsID;
	}

	public String getDetailContent() {
		return detailContent;
	}

	public void setDetailContent(String detailContent) {
		this.detailContent = detailContent;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public Timestamp getPostDate() {
		return postDate;
	}

	public void setPostDate(Timestamp postDate) {
		this.postDate = postDate;
	}

	public Long getPublisher_id() {
		return publisher_id;
	}

	public void setPublisher_id(Long publisher_id) {
		this.publisher_id = publisher_id;
	}

	public String getPublisher_avatar_url() {
		return publisher_avatar_url;
	}

	public void setPublisher_avatar_url(String publisher_avatar_url) {
		this.publisher_avatar_url = publisher_avatar_url;
	}

	public String getPublisher_name() {
		return publisher_name;
	}

	public void setPublisher_name(String publisher_name) {
		this.publisher_name = publisher_name;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}
}
