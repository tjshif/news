package com.school.service.common;

import com.mysql.cj.jdbc.util.TimeUtil;
import com.school.AOP.CacheMethodLogo;
import com.school.DAO.IUserDao;
import com.school.Entity.CommentCountDTO;
import com.school.Entity.UserDTO;
import com.school.Redis.RedisHandler;
import com.school.Utils.TimeUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserCommonServiceUtil {
	@Resource
	private IUserDao userDao;

	@Resource
	private RedisHandler redisHandler;

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS)
	public UserDTO getUserDTOCacheByID(Long userID)
	{
		return getUserDTOByIDInDB(userID.toString());
	}

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS)
	public UserDTO getUserDTOCacheByNickName(String nickName)
	{
		return getUserDTOByNickName(nickName);
	}

	public UserDTO getUserDTOByIDInDB(String userID)
	{
		if (userID == null)
			return null;
		return userDao.selectByID(userID.toString());
	}

	public UserDTO getUserDTOByNickName(String nickName)
	{
		if (TextUtils.isEmpty(nickName))
			return null;
		return userDao.selectByNickName(nickName);
	}

	public Set<UserDTO> selectUsers(Set<String> publisherIDs)
	{
		if (publisherIDs == null || publisherIDs.size() == 0)
			return null;

		Set<String> notExistPublishers = new HashSet<>();
		Set<UserDTO> userDTOS = redisHandler.readItemsFromRedisByIDs(publisherIDs, UserDTO.class, notExistPublishers);
		if (notExistPublishers.size() > 0)
		{
			List<UserDTO> loadUsersFromDB = userDao.selectUsers(notExistPublishers);
			if (loadUsersFromDB.size() > 0)
			{
				redisHandler.loadItemsToRedis(loadUsersFromDB, TimeUtils.ONE_DAY_SECONDS);
				userDTOS.addAll(loadUsersFromDB);
			}
		}
		return userDTOS;
	}

	public UserDTO selectUser(String userID)
	{
		if (TextUtils.isEmpty(userID))
			return null;

		UserDTO userDTO = redisHandler.readItemFromRedis(UserDTO.getItemKey(userID), UserDTO.class);
		if (userDTO != null)
			return userDTO;
		userDTO = loadUserToRedis(userID);
		return userDTO;
	}

	public UserDTO loadUserToRedis(String userID)
	{
		UserDTO userDTO = getUserDTOByIDInDB(userID);
		if (userDTO == null)
			return null;
		redisHandler.loadItemToRedis(userDTO, TimeUtils.ONE_DAY_SECONDS);
		return userDTO;
	}

}
