package com.school.Enum;

public enum NewsJobSubEnum {
	SUB_FULLTIME(1),
	SUB_PARTTIME(2),
	SUB_INTERN(3);

	private Integer newsJobType;

	private NewsJobSubEnum(int type) {
		this.newsJobType = type;
	}

	public Integer getSiteCode() {
		return this.newsJobType;
	}
}
