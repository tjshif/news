package com.school.Gson;

public class LoginRegisterGson extends RetResultGson {
	private String	userId;
	private String nickName;
	private String phoneNo;
	private Boolean isRegister;

	public LoginRegisterGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setRegister(Boolean register) {
		isRegister = register;
	}

	public Boolean getRegister() {
		return isRegister;
	}
}
