package com.school.service;

import com.mysql.cj.jdbc.util.TimeUtil;
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
