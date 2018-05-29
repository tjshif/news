package com.school.Entity;

public class UserDTO extends BaseDTO{
	private String phoneNumber;
	private String nickName;
	private Boolean isVerified = false;
	private String avatarUrl;
	private Boolean sex;
	private String college;

	public UserDTO()
	{

	}

	public UserDTO(String phoneNo, String nickName)
	{
		setNickName(nickName);
		setPhoneNumber(phoneNo);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getCollege() {
		return college;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void setVerified(Boolean verified) {
		isVerified = verified;
	}

	public Boolean getVerified() {
		return isVerified;
	}

	@Override
	public String getKey(String id)
	{
		return String.format("User:%s", id);
	}
}
