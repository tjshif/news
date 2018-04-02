package com.school.Enum;

public enum NewsEnum {
	NEWS_JOB(2),
	NEWS_FRIENDS(3);

	private Integer newsType;

	private NewsEnum(int type) {
		this.newsType = type;
	}

	public Integer getNewsType() {
		return this.newsType;
	}

	/*方法Value2CityTest是为了typeHandler后加的*/
	public static NewsEnum valueToNews(int newsType) {
		for (NewsEnum newsEnum : NewsEnum.values()) {
			if (newsEnum.newsType == newsType) {
				return newsEnum;
			}
		}
		throw new IllegalArgumentException("无效的value值: " + newsType + "!");
	}
}

