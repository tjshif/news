package com.school.Enum;

public enum TagEnum {
	AD_TAG(1, "广告"),
	TOP_TAG(2, "置顶");

	private Integer tagValue;
	private String  tagName;

	TagEnum(int tagValue, String tagName)
	{
		this.tagValue = tagValue;
		this.tagName = tagName;
	}

	public Integer getTagValue() {
		return tagValue;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void setTagValue(Integer tagValue) {
		this.tagValue = tagValue;
	}

	public static Integer nameToTagValue(String tagName)
	{
		for (TagEnum tagEnum : TagEnum.values())
		{
			if (tagEnum.getTagName().equalsIgnoreCase(tagName))
				return tagEnum.getTagValue();
		}

		throw new IllegalArgumentException("invalid tagName: " + tagName + "!");
	}
}
