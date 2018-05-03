package com.school.Gson;

public class AvatarResultGson extends RetResultGson{
	private String avatarPath;

	public AvatarResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
}
