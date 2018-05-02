package com.school.Gson;

public class UserInfoResultGson extends RetResultGson{
	private String phoneNumber;
	private String nickName;
	private Boolean isVerified;
	private String avatarUrl;
	private Boolean sex;
	private String college;
	public UserInfoResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public Boolean getSex() {
		return sex;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Boolean getVerified() {
		return isVerified;
	}

	public void setVerified(Boolean verified) {
		isVerified = verified;
	}
}
