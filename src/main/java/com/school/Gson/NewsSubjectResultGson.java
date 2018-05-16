package com.school.Gson;

import com.school.Entity.NewsDTO;
import com.school.Utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsSubjectResultGson extends RetResultGson{
	private List<MsgGson> msgGsonList;

	public NewsSubjectResultGson(int retCode, String message) {
		super(retCode, message);
	}

	public void setMsgGsonList(List<MsgGson> newsList) {
		this.msgGsonList = newsList;
	}

	public List<MsgGson> getMsgGsonList() {
		return msgGsonList;
	}
}
