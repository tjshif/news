package com.school.service.common;

import com.school.AOP.CacheMethodLogo;
import com.school.DAO.IUserDao;
import com.school.Entity.UserDTO;
import com.school.Utils.TimeUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;

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

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS)
	public UserDTO getUserDTO(String nickName)
	{
		return getUserDTOWithoutCache(nickName);
	}

	public UserDTO getUserDTOWithoutCache(Long userID)
	{
		if (userID == null)
			return null;
		return userDao.selectByID(userID.toString());
	}

	public UserDTO getUserDTOWithoutCache(String nickName)
	{
		if (TextUtils.isEmpty(nickName))
			return null;
		return userDao.selectByNickName(nickName);
	}
}
