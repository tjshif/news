package com.school.Entity;

import java.sql.Timestamp;

public class PostmsgDTO extends MsgDTO{
	private String content;
	private String imagePaths;

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public String getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(String imagePaths) {
		this.imagePaths = imagePaths;
	}


}
