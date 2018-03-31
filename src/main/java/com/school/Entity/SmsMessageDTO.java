package com.school.Entity;

public class SmsMessageDTO extends BaseDTO {
	private String phoneNumber;
	private String code;
	private Integer count;

	public SmsMessageDTO(){

	}

	public String getCode() {
		return code;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
