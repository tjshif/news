package com.school.Gson;

import com.school.Entity.Channel;

import java.util.List;

public class ChannelResultGson extends RetResultGson {
	private List<Channel> channels;
	private Integer channelVersion;

	public ChannelResultGson(int retCode, String message) {
		super(retCode, message);
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public void setChannelVersion(Integer channelVersion) {
		this.channelVersion = channelVersion;
	}

	public Integer getChannelVersion() {
		return channelVersion;
	}
}
