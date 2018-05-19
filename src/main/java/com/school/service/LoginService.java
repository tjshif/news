package com.school.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.school.AOP.CacheMethodLogo;
import com.school.AOP.LogAnnotation;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IFeedbackDao;
import com.school.DAO.ISmsMessageDao;
import com.school.DAO.IUserDao;
import com.school.DAO.IVersionDao;
import com.school.Entity.FeedbackDTO;
import com.school.Entity.SmsMessageDTO;
import com.school.Entity.UserDTO;
import com.school.Entity.VersionDTO;
import com.school.Gson.LoginRegisterGson;
import com.school.Gson.RetResultGson;
import com.school.Gson.VersionResultGson;
import com.school.Utils.SendSmsUtil;
import com.school.Utils.TimeUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService {
	Logger logger = Logger.getLogger(LoginService.class.getName());

	@Resource
	private ISmsMessageDao smsMessageDao;

	@Resource
	private IUserDao userDao;

	@Resource
	private IFeedbackDao feedbackDao;

	@Resource
	private IVersionDao versionDao;

	@Transactional
	public LoginRegisterGson loginUser(String phoneNo)
	{
		LoginRegisterGson logRegGson = new LoginRegisterGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		String nickName = "SD_" + phoneNo;
		UserDTO userDTO = new UserDTO(phoneNo, nickName);
		try
		{
			userDao.insert(userDTO);
			nickName = randString(10) + userDTO.getId();
			Integer row = userDao.updateNickName(Long.parseLong(userDTO.getId()), nickName);
			if (row == 1)
			{
				userDTO = userDao.selectByPhoneNo(phoneNo);
				logRegGson.setRegister(true);
			}
		}
		catch (DuplicateKeyException ex)
		{
			userDTO = userDao.selectByPhoneNo(phoneNo);
			logRegGson.setRegister(false);
		}

		logRegGson.setAvatarUrl(userDTO.getAvatarUrl());
		logRegGson.setUserId(userDTO.getId());
		logRegGson.setPhoneNo(userDTO.getPhoneNumber());
		logRegGson.setNickName(userDTO.getNickName());
		return logRegGson;
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
				logRegGson = loginUser(phoneNumber);
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			logRegGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return logRegGson;
	}

	private String randString(int nCount)
	{
		String result = "";
		String chars = "abcdefghijklmnopqrstuvwxyz";
		for (int ii = 0; ii < nCount; ++ii)
		{
			result += chars.charAt((int)(Math.random() * 26));
		}
		return result;
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

	@Transactional
	public RetResultGson sendSms(String phoneNumber)
	{
		RetResultGson retResult = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		Integer code = (int)(Math.random() * 10000);
		try
		{
			SmsMessageDTO smsMessageDTO = smsMessageDao.selectSmsSendToday(phoneNumber);
			if (smsMessageDTO != null)
			{
				if ( smsMessageDTO.getCount() >= 5)
				{
					retResult.setResult(RetCode.RET_ERROR_CODE_SENDSMSMAXCOUNT, RetMsg.RET_MESSAGE_SENDSMSMAXCOUNT);
					return retResult;
				}
				else
					smsMessageDTO.setCount(smsMessageDTO.getCount() + 1);
			}
			else
			{
				smsMessageDTO = new SmsMessageDTO();
				smsMessageDTO.setCount(1);
			}
			smsMessageDTO.setCode(String.format("%04d", code));
			smsMessageDTO.setPhoneNumber(phoneNumber);
			smsMessageDao.insert(smsMessageDTO);

			SendSmsResponse sendSmsResponse = SendSmsUtil.sendSms(phoneNumber, smsMessageDTO.getCode());
			if (sendSmsResponse.getCode() == null || !sendSmsResponse.getCode().equals("OK"))
			{
				logger.error("code: " + sendSmsResponse.getCode() + " ;Msg:" +sendSmsResponse.getMessage());
				retResult.setResult(RetCode.RET_CODE_SMSERROR, RetMsg.RET_MSG_SMSERROR);
				return retResult;
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			retResult.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return retResult;
	}

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS * 5)
	public VersionResultGson getVersionInfo(Long id)
	{
		VersionResultGson resultGson = new VersionResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try
		{
			List<VersionDTO> versionDTOS = versionDao.isLatestVersion(id);
			if (versionDTOS.size() == 0)
			{
				resultGson.setID(id.toString());
				return resultGson;
			}

			List<String> comments = new ArrayList<>();
			Boolean isForceUpdate = false;
			for (int ii = 0; ii < versionDTOS.size(); ++ii)
			{
				VersionDTO versionDTO = versionDTOS.get(ii);
				if (versionDTO == null || TextUtils.isEmpty(versionDTO.getComments()))
					continue;

				comments.add(versionDTO.getComments());
				if (versionDTO.getForce())
					isForceUpdate = true;
			}
			resultGson.setComments(comments);
			resultGson.setVersion(versionDTOS.get(0).getVersion());
			resultGson.setForce(isForceUpdate);
			resultGson.setID(versionDTOS.get(0).getId());
			resultGson.setLinkurl(versionDTOS.get(0).getLinkurl());
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}
}
