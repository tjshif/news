package com.school.Enum;

public enum NewsEnum {
	NEWS_JOB(2, "工作"),
	NEWS_FRIENDS(3, "鹊桥");

	private Integer newsType;
	private String channelName;

	NewsEnum(int type, String name) {
		this.newsType = type;
		this.channelName = name;
	}

	public Integer getNewsType() {
		return this.newsType;
	}

	public String getChannelName() {
		return channelName;
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

