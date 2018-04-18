package com.school.Gson;

import java.util.List;

public class VersionResultGson extends RetResultGson{
	private String ID;
	private String version;
	private Boolean isForce;
	private String Linkurl;
	private List<String> comments;

	public VersionResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public void setForce(Boolean force) {
		isForce = force;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public void setLinkurl(String linkurl) {
		Linkurl = linkurl;
	}
}
