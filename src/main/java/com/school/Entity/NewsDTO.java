package com.school.Entity;

import java.sql.Timestamp;

public class NewsDTO extends BaseDTO {
	private String 	subject;
	private String		imagePaths;
	private Integer	newsType;
	private Integer	newsSubType;
	private Timestamp 	postDate;	//post的时间
	private Integer    locationCode;	//地点，可以更加学校所在地填写
	private Boolean 	isHot;
	private Boolean 	isValid = true;
	private Boolean	hasDetail; // 是否有详细内容
	private Long 		publisherId;
	private String 	source;

	public String getContent() {
		return subject;
	}

	public void setImagePaths(String imagePaths) {
		this.imagePaths = imagePaths;
	}

	public Integer getNewsType() {
		return newsType;
	}

	public void setPostDate(Timestamp postDate) {
		this.postDate = postDate;
	}

	public Integer getNewsSubType() {
		return newsSubType;
	}

	public void setNewsSubType(Integer newsSubType) {
		this.newsSubType = newsSubType;
	}

	public Timestamp getPostDate() {
		return postDate;
	}

	public void setNewsType(Integer newsType) {
		this.newsType = newsType;
	}

	public String getImagePaths() {
		return imagePaths;
	}

	public void setContent(String content) {
		this.subject = content;
	}

	public Boolean getHot() {
		return isHot;
	}

	public Integer getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(Integer locationCode) {
		this.locationCode = locationCode;
	}

	public void setHasDetail(Boolean hasDetail) {
		this.hasDetail = hasDetail;
	}

	public void setHot(Boolean hot) {
		isHot = hot;
	}

	public Boolean getHasDetail() {
		return hasDetail;
	}

	public Boolean getValid() {
		return isValid;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setValid(Boolean valid) {
		isValid = valid;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String getKey(String id)
	{
		return getNewsItemKey(id);
	}

	public static String getNewsItemKey(String id)
	{
		return "News:" + id;
	}

	public static String getNewsTypeLocationKey(Integer newsType, Integer location)
	{
		if (location == null || newsType == null)
			return "";
		return String.format("type:%d;location:%d", newsType, location);
	}

	public static String getNewsTypeSubTypeLocationKey(Integer newsType, Integer subNewsType, Integer location)
	{
		if (newsType == null || location == null || subNewsType == null)
			return "";
		return String.format("type:%d;subtype:%d;loation:%d", newsType, subNewsType, location);
	}
}
