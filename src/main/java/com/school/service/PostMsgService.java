package com.school.service;

import com.google.gson.JsonSyntaxException;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IPostmsgDao;
import com.school.Entity.PostmsgDTO;
import com.school.Gson.PostMsgGson;
import com.school.Gson.RetResultGson;
import com.school.PushService.PushPostMsgToDBService;
import com.school.Redis.LoadDateToRedis;
import com.school.Utils.GsonUtil;
import com.school.Utils.IdWorkerUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PostMsgService {
	private Logger logger = Logger.getLogger(PostMsgService.class.getName());

	@Resource
	IPostmsgDao postmsgDao;

	@Resource
	LoadDateToRedis loadDataToRedis;

	@Resource
	PushPostMsgToDBService pushPostMsgToDBService;

	public RetResultGson postMsgToRedis(Long userID, String msgdto, List<String> msgImageFiles)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			PostMsgGson postMsgGson = GsonUtil.fromJson(msgdto, PostMsgGson.class);

			//load data to redis
			PostmsgDTO postmsgDTO = new PostmsgDTO();
			postmsgDTO.setContent(postMsgGson.getContent());
			postmsgDTO.setLocationCode(postMsgGson.getLocationCode());
			postmsgDTO.setNewsType(postMsgGson.getNewsType());
			postmsgDTO.setNewsSubType(postMsgGson.getNewsSubType());
			postmsgDTO.setId(IdWorkerUtils.getGlobalID().toString());
			postmsgDTO.setPublisherId(userID);
			postmsgDTO.setPostDate(new Timestamp(System.currentTimeMillis()));
			if(msgImageFiles != null && msgImageFiles.size() > 0)
			{
				String filePath = String.join(",", msgImageFiles);
				postmsgDTO.setImagePaths(filePath);
			}
			loadDataToRedis.loadPostMsgToRedis(postmsgDTO);

			//Post msg to activemq to store data to db.
			pushPostMsgToDBService.push(postmsgDTO);
		}
		catch (JsonSyntaxException ex)
		{
			logger.error(ex);
			resultGson.setResult(RetCode.RET_ERROR_INVLAID_GSON_STRING, RetMsg.RET_MSG_INVLAID_GSON_STRING);
			return resultGson;
		}
		catch (Exception ex)
		{
			logger.error(ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public RetResultGson postMsg(Long userID, String msgdto, List<String> msgImageFiles)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			PostMsgGson postMsgGson = GsonUtil.fromJson(msgdto, PostMsgGson.class);
			PostmsgDTO postmsgDTO = new PostmsgDTO();
			postmsgDTO.setContent(postMsgGson.getContent());
			postmsgDTO.setLocationCode(postMsgGson.getLocationCode());
			postmsgDTO.setNewsType(postMsgGson.getNewsType());
			postmsgDTO.setNewsSubType(postMsgGson.getNewsSubType());
			postmsgDTO.setId(IdWorkerUtils.getGlobalID().toString());
			postmsgDTO.setPublisherId(userID);
			if(msgImageFiles != null && msgImageFiles.size() > 0)
			{
				String filePath = String.join(",", msgImageFiles);
				postmsgDTO.setImagePaths(filePath);
			}
			postmsgDao.insert(postmsgDTO);
		}
		catch (JsonSyntaxException ex)
		{
			logger.error(ex);
			resultGson.setResult(RetCode.RET_ERROR_INVLAID_GSON_STRING, RetMsg.RET_MSG_INVLAID_GSON_STRING);
			return resultGson;
		}
		catch (Exception ex)
		{
			logger.error(ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}
}
