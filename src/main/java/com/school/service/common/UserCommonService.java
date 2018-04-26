package com.school.service.common;

import com.mysql.cj.jdbc.util.TimeUtil;
import com.school.AOP.CacheMethodLogo;
import com.school.DAO.IUserDao;
import com.school.Entity.UserDTO;
import com.school.Utils.TimeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserCommonService {
	@Resource
	private IUserDao userDao;

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS)
	protected UserDTO getUserDTO(Long userID)
	{
		if (userID == null)
			return null;
		return userDao.selectByID(userID);
	}
}
