package com.school.Gson;

public class UserInfoGson {
	private Long ID;
	private String nickName;
	private Boolean sex;
	private String college;

	public Long getID() {
		return ID;
	}

	public void setID(Long ID) {
		this.ID = ID;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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
}
