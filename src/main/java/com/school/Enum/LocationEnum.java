package com.school.Enum;

public enum  LocationEnum {
	ALL(0),
	SHANGHAI(21),
	NANJING(25);

	private Integer zipCode;

	private LocationEnum(Integer zipCode) {
		this.zipCode = zipCode;
	}

	public int getZipCode() {
		return this.zipCode;
	}
}
