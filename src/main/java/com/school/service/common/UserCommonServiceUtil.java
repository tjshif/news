package com.school.service.common;

import com.mysql.cj.jdbc.util.TimeUtil;
import com.school.AOP.CacheMethodLogo;
import com.school.DAO.IUserDao;
import com.school.Entity.UserDTO;
import com.school.Utils.TimeUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Component
public class UserCommonServiceUtil {
	@Resource
	private IUserDao userDao;

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS)
	public UserDTO getUserDTO(Long userID)
	{
		return getUserDTOWithoutCache(userID);
	}

	public UserDTO getUserDTOWithoutCache(Long userID)
	{
		if (userID == null)
			return null;
		return userDao.selectByID(userID);
	}
}
