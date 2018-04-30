package com.school.Entity;

import java.sql.Timestamp;

public class NewsDTO extends BaseDTO {
	private String 	subject;
	private Integer	newsType;
	private Integer	newsSubType;
	private Timestamp	postDate;	//post的时间
	private Integer    locationCode;	//地点，可以更加学校所在地填写
	private Integer    isHot;		//是否热点
	private String     linkUrl;
	private Long 		publisherId;
	private String     publishSource;
	private Integer	commentCount;

	public Integer getNewsType() {
		return newsType;
	}

	public Integer getLocation() {
		return locationCode;
	}

	public Integer getNewsSubType() {
		return newsSubType;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublishSource(String publishSource) {
		this.publishSource = publishSource;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
}
