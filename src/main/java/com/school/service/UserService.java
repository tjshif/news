package com.school.service;

import com.school.Constants.EnvConst;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IUserDao;
import com.school.Entity.UserDTO;
import com.school.Gson.AvatarResultGson;
import com.school.Gson.RetResultGson;
import com.school.Gson.UserInfoGson;
import com.school.Gson.UserInfoResultGson;
import com.school.service.common.UserCommonServiceUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Service
public class UserService {
	private Logger logger = Logger.getLogger(UserService.class.getName());

	@Resource
	private IUserDao userDao;

	@Resource
	private UserCommonServiceUtil userCommonServiceUtil;

	public RetResultGson updateUserNickName(Long userID, String nickName)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			userDao.updateNickName(userID, nickName);
		}
		catch (DuplicateKeyException ex)
		{
			resultGson.setResult(RetCode.RET_ERROR_USRE_INVALID_NICKNAME, RetMsg.RET_MSG_USRE_INVALID_NICKNAME);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public RetResultGson updateSex(Long userID, Boolean sex)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try
		{
			userDao.updateSex(userID, sex);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public RetResultGson updateCollege(Long userID, String college)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			userDao.updateCollege(userID, college);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public RetResultGson updateAvatarUrl(Long userID, String avatarUrl)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			userDao.updateAvatarUrl(userID, avatarUrl);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public UserInfoResultGson updateUserInfo(Long userID, UserInfoGson userInfoGson)
	{
		if (userID == null || userInfoGson == null)
			return null;

		UserInfoResultGson resultGson = new UserInfoResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			userInfoGson.setID(userID);
			userDao.updateUserInfo(userInfoGson);
			resultGson = getUserInfo(userID);
		}
		catch (DuplicateKeyException ex)
		{
			resultGson.setResult(RetCode.RET_ERROR_USRE_INVALID_NICKNAME, RetMsg.RET_MSG_USRE_INVALID_NICKNAME);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public UserInfoResultGson getUserInfo(Long userID)
	{
		UserInfoResultGson resultGson = new UserInfoResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			UserDTO userDTO = userCommonServiceUtil.getUserDTOWithoutCache(userID);
			resultGson.setAvatarUrl(userDTO.getAvatarUrl());
			resultGson.setPhoneNumber(userDTO.getPhoneNumber());
			resultGson.setCollege(userDTO.getCollege());
			resultGson.setNickName(userDTO.getNickName());
			resultGson.setSex(userDTO.getSex());
			resultGson.setVerified(userDTO.getVerified());
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public AvatarResultGson updateUserAvatarPath(Long userID, String imageFilePath)
	{
		AvatarResultGson resultGson = new AvatarResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		UserDTO userDTO = userCommonServiceUtil.getUserDTOWithoutCache(userID);
		if (userDTO == null)
		{
			resultGson.setResult(RetCode.RET_ERROR_INVALID_USERID, RetMsg.RET_MSG_INVALID_USERID);
			return resultGson;
		}

		String originalImagePath = userDTO.getAvatarUrl();
		if (!TextUtils.isEmpty(originalImagePath) && originalImagePath.equalsIgnoreCase(imageFilePath))
		{
			resultGson.setAvatarPath(imageFilePath);
			return resultGson;
		}

		RetResultGson updateResult = updateAvatarUrl(userID, imageFilePath);
		if (updateResult.getRetCode() == RetCode.RET_CODE_OK)
		{//remove old file
			removeFile(originalImagePath);
			resultGson.setAvatarPath(imageFilePath);
		}
		else
		{
			resultGson.setResult(updateResult.getRetCode(), updateResult.getMessage());
		}
		return resultGson;
	}

	public void removeFile(String fileName)
	{
		if (TextUtils.isEmpty(fileName))
			return;

		String filePath = EnvConst.ROOT_FOLDER + fileName;
		File file = new File(filePath);
		if (file.exists())
		{
			try {
				FileUtils.forceDelete(file);
			}
			catch (IOException ex)
			{
				logger.error("Fail to delete file: " + filePath + ex.getMessage());
			}
		}
		return;
	}

}
