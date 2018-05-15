package com.school.Utils;

import com.school.Entity.MsgDTO;
import com.school.Entity.NewsDTO;
import com.school.Entity.PostmsgDTO;
import com.school.Gson.MsgGson;
import org.apache.http.util.TextUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvertUtils {

	public static MsgGson convertToMsgGson(NewsDTO newsDTO)
	{
		MsgGson msgGson = convertToCommonMsgGson(newsDTO);
		msgGson.setContent(newsDTO.getContent());
		return msgGson;
	}

	public static MsgGson convertToMsgGson(PostmsgDTO postmsgDTO)
	{
		MsgGson msgGson = convertToCommonMsgGson(postmsgDTO);
		msgGson.setContent(postmsgDTO.getContent());
		//imagepath
		if (!TextUtils.isEmpty(postmsgDTO.getImagePaths()))
		{
			String[] imagePaths = postmsgDTO.getImagePaths().split(",");
			List<String> paths = Arrays.asList(imagePaths);
			msgGson.setImagePaths(paths);
		}
		return msgGson;
	}

	private static MsgGson convertToCommonMsgGson(MsgDTO msgDTO)
	{
		MsgGson msgGson = new MsgGson();
		msgGson.setID(msgDTO.getId());
		msgGson.setNewsType(msgDTO.getNewsType());
		msgGson.setNewsSubType(msgDTO.getNewsSubType());
		msgGson.setLocationCode(msgDTO.getLocationCode());
		msgGson.setPostDate(msgDTO.getPostDate());
		msgGson.setHot(msgDTO.getHot());
		msgGson.setCreateAt(msgDTO.getCreateAt());
		msgGson.setCreateBy(msgDTO.getCreateBy());
		msgGson.setUpdateAt(msgDTO.getUpdateAt());
		msgGson.setUpdateBy(msgDTO.getUpdateBy());
		msgGson.setPublisherId(msgDTO.getPublisherId());
		msgGson.setPublishSource(msgDTO.getPublishSource());
		return msgGson;
	}
}
