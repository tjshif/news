package com.school.Utils;

import com.school.Entity.NewsDTO;
import com.school.Gson.MsgGson;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvertUtils {

	public static List<MsgGson> convertToMsgGsonList(List<NewsDTO> newsDTOS)
	{
		List<MsgGson> msgGsons = new ArrayList<>();
		for (NewsDTO newsDTO : newsDTOS)
		{
			msgGsons.add(ConvertUtils.convertToMsgGson(newsDTO));
		}
		return msgGsons;
	}

	public static MsgGson convertToMsgGson(NewsDTO newsDTO)
	{
		MsgGson msgGson = convertToCommonMsgGson(newsDTO);
		msgGson.setContent(newsDTO.getContent());

		//imagepath
		if (!TextUtils.isEmpty(newsDTO.getImagePaths()))
		{
			String[] imagePaths = newsDTO.getImagePaths().split(",");
			List<String> paths = Arrays.asList(imagePaths);
			msgGson.setImagePaths(paths);
		}
		return msgGson;
	}

	private static MsgGson convertToCommonMsgGson(NewsDTO newsDTO)
	{
		MsgGson msgGson = new MsgGson();
		msgGson.setID(newsDTO.getId());
		msgGson.setNewsType(newsDTO.getNewsType());
		msgGson.setNewsSubType(newsDTO.getNewsSubType());
		msgGson.setLocationCode(newsDTO.getLocationCode());
		msgGson.setPostDate(newsDTO.getPostDate());
		msgGson.setHot(newsDTO.getHot());
		msgGson.setCreateAt(newsDTO.getCreateAt());
		msgGson.setCreateBy(newsDTO.getCreateBy());
		msgGson.setUpdateAt(newsDTO.getUpdateAt());
		msgGson.setUpdateBy(newsDTO.getUpdateBy());
		msgGson.setPublisherId(newsDTO.getPublisherId());
		msgGson.setPublishSource(newsDTO.getPublishSource());
		return msgGson;
	}
}
