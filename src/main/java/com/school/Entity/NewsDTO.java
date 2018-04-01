package com.school.Entity;

import java.sql.Timestamp;

public class NewsDTO extends BaseDTO {
	private String 	subject;
	private Integer	newType;
	private Integer	newSubEnum;
	private Timestamp	postDate;	//post的时间
	private Integer    location;	//地点，可以更加学校所在地填写
	private Integer    isHot;		//是否热点
}
