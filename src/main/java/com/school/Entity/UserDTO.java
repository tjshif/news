package com.school.Entity;

public class UserDTO extends BaseDTO{
	private String phoneNumber;
	private String nickName;

	public UserDTO()
	{

	}

	public UserDTO(String phoneNo, String nickName)
	{
		setNickName(nickName);
		setPhoneNo(phoneNo);
	}

	public String getPhoneNo() {
		return phoneNumber;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNumber = phoneNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
