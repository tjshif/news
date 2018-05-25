package com.school.Entity;

public class Channel {
	private String channelName;
	private Integer channelCode;

	public Channel(Integer channelCode, String channelName)
	{
		setChannelCode(channelCode);
		setChannelName(channelName);
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
