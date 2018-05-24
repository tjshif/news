package com.school.service;

import com.google.gson.JsonSyntaxException;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Entity.MsgAggregate;
import com.school.Entity.NewsDTO;
import com.school.Entity.NewsDetailDTO;
import com.school.Gson.PostMsgGson;
import com.school.Gson.RetResultGson;
import com.school.PushService.PushPostMsgToDBService;
import com.school.Redis.LoadDataToRedis;
import com.school.Utils.GsonUtil;
import com.school.Utils.IdWorkerUtils;
import com.school.Utils.MessageUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.soap.Text;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PostMsgService {
	private Logger logger = Logger.getLogger(PostMsgService.class.getName());

	@Resource
	LoadDataToRedis loadDataToRedis;

	@Resource
	MessageUtils messageUtils;

	public RetResultGson postMsgToRedis(Long userID, String msgdto, List<String> msgImageFiles)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			PostMsgGson postMsgGson = GsonUtil.fromJson(msgdto, PostMsgGson.class);

			//load data to redis
			NewsDTO newsDTO = new NewsDTO();
			newsDTO.setContent(postMsgGson.getContent());
			newsDTO.setLocationCode(postMsgGson.getLocationCode());
			newsDTO.setNewsType(postMsgGson.getNewsType());
			newsDTO.setNewsSubType(postMsgGson.getNewsSubType());
			if (!TextUtils.isEmpty(postMsgGson.getID()))
				newsDTO.setId(postMsgGson.getID());
			else
				newsDTO.setId(IdWorkerUtils.getGlobalID().toString());

			newsDTO.setPublisherId(userID);
			newsDTO.setUpdateBy("postMsgToRedis");
			newsDTO.setSource(postMsgGson.getSource());
			if (postMsgGson.getPostDate() != null)
				newsDTO.setPostDate(postMsgGson.getPostDate());
			else
				newsDTO.setPostDate(new Timestamp(System.currentTimeMillis()));
			newsDTO.setCreateAt(new Timestamp(System.currentTimeMillis()));
			newsDTO.setHasDetail(postMsgGson.getHasDetail());
			if(msgImageFiles != null && msgImageFiles.size() > 0)
			{
				String filePath = String.join(",", msgImageFiles);
				newsDTO.setImagePaths(filePath);
			}
			loadDataToRedis.loadNewsToRedis(newsDTO);

			MsgAggregate msgAggregate = new MsgAggregate();
			msgAggregate.setNewsDTO(newsDTO);
			if (postMsgGson.getHasDetail() || !TextUtils.isEmpty(postMsgGson.getSourceArticleUrl()))
			{//NewsDetail
				NewsDetailDTO newsDetailDTO = new NewsDetailDTO();
				newsDetailDTO.setDetailContent(postMsgGson.getDetailContent());
				newsDetailDTO.setNewsID(newsDTO.getId());
				newsDetailDTO.setSourceArticleUrl(postMsgGson.getSourceArticleUrl());
				newsDetailDTO.setUpdateBy("postMsgToRedis");
				msgAggregate.setNewsDetailDTO(newsDetailDTO);
			}

			//Post msg to activemq to store data to db.
			messageUtils.pushPostMsg(msgAggregate);
		}
		catch (JsonSyntaxException ex)
		{
			logger.error("",ex);
			resultGson.setResult(RetCode.RET_ERROR_INVLAID_GSON_STRING, RetMsg.RET_MSG_INVLAID_GSON_STRING);
			return resultGson;
		}
		catch (Exception ex)
		{
			logger.error("",ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

}
