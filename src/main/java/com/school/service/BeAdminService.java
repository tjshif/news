package com.school.service;

import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IBeAdminDao;
import com.school.Entity.BeAdminDTO;
import com.school.Gson.RetBeAdminLoginGson;
import com.school.service.common.BeAdminServiceUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class BeAdminService {
	private Logger logger = Logger.getLogger(BeAdminService.class.getName());

	@Resource
	private BeAdminServiceUtils beAdminServiceUtils;

	@Resource
	private IBeAdminDao beAdminDao;

	public RetBeAdminLoginGson adminLoginIn(String userName, String password)
	{
		RetBeAdminLoginGson resultGson =  new RetBeAdminLoginGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			BeAdminDTO beAdminDTO = beAdminDao.selectByUserName(userName);

			if (beAdminDTO == null || !beAdminDTO.getPassword().equals(password))
			{
				resultGson.setResult(RetCode.RET_ERROR_USER_INVALID_INFO, RetMsg.RET_MSG_USER_INVALID_INFO);
				return resultGson;
			}
			String sessionID = UUID.randomUUID().toString();
			beAdminServiceUtils.setBeAdminSession(Long.parseLong(beAdminDTO.getId()), sessionID);
			resultGson.setSessionID(sessionID);
			resultGson.setID(beAdminDTO.getId());
		}
		catch (Exception ex)
		{
			logger.error(ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}
}
