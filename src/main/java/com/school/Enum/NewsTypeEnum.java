package com.school.Enum;

public enum NewsTypeEnum {
	NEWS_JOB(2),
	NEWS_FRIENDS(3);

	private Integer newsType;

	private NewsTypeEnum(int type) {
		this.newsType = type;
	}

	public Integer getNewsTypeCode() {
		return this.newsType;
	}

	/*方法Value2CityTest是为了typeHandler后加的*/
	public static NewsTypeEnum valueToNews(int newsType) {
		for (NewsTypeEnum newsEnum : NewsTypeEnum.values()) {
			if (newsEnum.newsType == newsType) {
				return newsEnum;
			}
		}
		throw new IllegalArgumentException("无效的value值: " + newsType + "!");
	}
}
