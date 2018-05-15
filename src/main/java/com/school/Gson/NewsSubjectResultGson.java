package com.school.Gson;

import com.school.Entity.NewsDTO;
import com.school.Entity.PostmsgDTO;
import com.school.Utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsSubjectResultGson extends RetResultGson{
	private List<MsgGson> msgGsonList;

	public NewsSubjectResultGson(int retCode, String message)
	{
		super(retCode, message);
	}
	public void setMsgGsonList(List<MsgGson> newsList) {
		this.msgGsonList = newsList;
	}

	public List<MsgGson> getNewsList() {
		return msgGsonList;
	}

	public void setMsgGsonListByNewsList(List<NewsDTO> newsDTOS)
	{
		msgGsonList = new ArrayList<>();
		for (NewsDTO newsDTO : newsDTOS)
		{
			msgGsonList.add(ConvertUtils.convertToMsgGson(newsDTO));
		}
	}

	public void setMsgGsonListByPostMsgList(List<PostmsgDTO> postmsgDTOS)
	{
		msgGsonList = new ArrayList<>();
		for (PostmsgDTO postmsgDTO : postmsgDTOS)
		{
			msgGsonList.add(ConvertUtils.convertToMsgGson(postmsgDTO));
		}
	}
}
