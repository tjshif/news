package com.school.service;

import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IUnReadMesssageDao;
import com.school.Entity.UnReadMesssageDTO;
import com.school.Gson.RetResultGson;
import com.school.Gson.UnreadMsgResultGson;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UnreadMsgService {
	Logger logger = Logger.getLogger(UnreadMsgService.class.getName());

	@Resource
	private IUnReadMesssageDao unReadMesssageDao;

	public UnreadMsgResultGson getUnreadMsgs(Long userID)
	{
		UnreadMsgResultGson resultGson =  new UnreadMsgResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			UnReadMesssageDTO unReadMesssageDTO = unReadMesssageDao.selectByUserID(userID);
			resultGson.setUnReadMesssageDTO(unReadMesssageDTO);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public RetResultGson clearUnRead(Long userID)
	{
		RetResultGson resultGson =  new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			unReadMesssageDao.clearUnRead(userID);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}
}
