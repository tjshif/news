package com.school.service;

import com.school.AOP.LogAnnotation;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IFeedbackDao;
import com.school.DAO.ISmsMessageDao;
import com.school.DAO.IUserDao;
import com.school.Entity.FeedbackDTO;
import com.school.Entity.SmsMessageDTO;
import com.school.Entity.UserDTO;
import com.school.Gson.LoginRegisterGson;
import com.school.Gson.RetResultGson;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class LoginService {
	@Resource
	private ISmsMessageDao smsMessageDao;

	@Resource
	private IUserDao userDao;

	@Resource
	private IFeedbackDao feedbackDao;

	Logger logger = Logger.getLogger(LoginService.class.getName());

	@Transactional
	public UserDTO createUser(String phoneNo, String nickName)
	{
		UserDTO userDTO = new UserDTO(phoneNo, nickName);
		try
		{
			userDao.insert(userDTO);
		}
		catch (DuplicateKeyException ex)
		{
			userDTO = userDao.selectByPhoneNo(phoneNo);
		}
		return userDTO;
	}

	public LoginRegisterGson loginRegister(String phoneNumber, String smsCode)
	{
		LoginRegisterGson logRegGson = new LoginRegisterGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try
		{
			SmsMessageDTO smsMessageDTO = smsMessageDao.selectSmsSendToday(phoneNumber);
			if (smsMessageDTO == null)
			{
				logRegGson.setResult(RetCode.RET_CODE_GETSMSFIRST, RetMsg.RET_MSG_GETSMSFIRST);
				return logRegGson;
			}
			if (!smsMessageDTO.getCode().equals(smsCode))
				logRegGson.setResult(RetCode.RET_CODE_SMSERROR, RetMsg.RET_MSG_SMSERROR);
			else
			{
				String nickName = "SD_" + phoneNumber;
				UserDTO userDTO = createUser(phoneNumber, nickName);
				logRegGson.setUserId(userDTO.getId());
				logRegGson.setPhoneNo(userDTO.getPhoneNo());
				logRegGson.setNickName(userDTO.getNickName());
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			logRegGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return logRegGson;
	}

	public RetResultGson insertFeedback(String contactInfo, String feedback)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try
		{
			FeedbackDTO feedbackDTO = new FeedbackDTO(contactInfo, feedback);
			feedbackDao.insert(feedbackDTO);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}
}
