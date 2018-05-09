package com.school.service.common;

import com.mysql.cj.jdbc.util.TimeUtil;
import com.school.DAO.IBeAdminDao;
import com.school.Entity.BeAdminDTO;
import com.school.Redis.LoadDateToRedis;
import com.school.Redis.ReadDataFromRedis;
import com.school.Utils.TimeUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class BeAdminServiceUtils {
	private Logger logger = Logger.getLogger(BeAdminServiceUtils.class.getName());

	@Resource
	LoadDateToRedis loadDateToRedis;

	@Resource
	ReadDataFromRedis readDataFromRedis;

	@Resource
	private IBeAdminDao beAdminDao;

	public Boolean isLoginIn(Long adminID, String sessionID)
	{
		if (TextUtils.isEmpty(sessionID))
			return false;

		String storedSessionID = readDataFromRedis.readSession(adminID);
		if (TextUtils.isEmpty(storedSessionID))
		{
			storedSessionID = beAdminDao.getSessionWithinHalfHour(adminID);
			if (TextUtils.isEmpty(storedSessionID))
				return false;
		}

		if (sessionID.equalsIgnoreCase(storedSessionID))
			return true;
		return false;
	}

	public void setBeAdminSession(Long adminID, String sessionID)
	{
		if (TextUtils.isEmpty(sessionID))
		{
			logger.error("invalid sessionID");
			return;
		}

		Integer row = beAdminDao.updateSessionID(adminID, sessionID);
		if (row == 1)
		{
			loadDateToRedis.LoadSession(adminID, TimeUtils.ONE_MINUTE_SECONDS * 30, sessionID);
		}
	}
}
