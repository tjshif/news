package com.school.Entity;

public class VersionDTO extends BaseDTO {
	private String Version;
	private Boolean isForce;
	private String  Comments;
	private String  Linkurl;

	public String getComments() {
		return Comments;
	}

	public String getVersion() {
		return Version;
	}

	public Boolean getForce() {
		return isForce;
	}

	public String getLinkurl() {
		return Linkurl;
	}
}
