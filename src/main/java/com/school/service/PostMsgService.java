package com.school.service;

import com.google.gson.JsonSyntaxException;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Entity.PostmsgDTO;
import com.school.Gson.PostMsgGson;
import com.school.Gson.RetResultGson;
import com.school.Utils.GsonUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostMsgService {
	private Logger logger = Logger.getLogger(PostMsgService.class.getName());

	public RetResultGson postMsg(String msgdto, List<String> msgImageFiles)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			PostMsgGson postMsgGson = GsonUtil.fromJson(msgdto, PostMsgGson.class);
			PostmsgDTO postmsgDTO = new PostmsgDTO();
			postmsgDTO.setContent(postMsgGson.getContent());
			postmsgDTO.setLocationCode(postMsgGson.getLocationCode());
			postmsgDTO.setNewsType(postMsgGson.getNewsType());
			postmsgDTO.setNewsSubType(postMsgGson.getNewsSubType());
			if(msgImageFiles != null && msgImageFiles.size() > 0)
			{
				String filePath = String.join(",", msgImageFiles);
				postmsgDTO.setImagePaths(filePath);
			}
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