package com.school.Redis;

import com.school.DAO.IUserDao;
import com.school.Entity.NewsDTO;
import com.school.Utils.GsonUtil;
import com.school.service.common.UserCommonServiceUtil;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisHandler {
	private Logger logger = Logger.getLogger(RedisHandler.class.getName());

	@Resource
	private IUserDao userDao;

	@Resource
	protected StoredCacheService storedCacheService;

	@Resource
	private UserCommonServiceUtil userCommonServiceUtil;

	protected String getNewsTypeLocationKey(Integer newsType, Integer location)
	{
		if (location == null || newsType == null)
			return "";
		return String.format("type:%d;location:%d", newsType, location);
	}

	protected String getNewsTypeSubTypeLocationKey(Integer newsType, Integer subNewsType, Integer location)
	{
		if (newsType == null || location == null || subNewsType == null)
			return "";
		return String.format("type:%d;subtype:%d;loation:%d", newsType, subNewsType, location);
	}

	protected String getNewsItemKey(String id)
	{
		return "News:" + id;
	}

	protected String getTrimKey()
	{
		return "TRIMKEYS";
	}

	public void removeNewsFromRedis(NewsDTO newsItem)
	{
		if (newsItem == null)
			return;

		//remove msgitem
		String key = getNewsItemKey(newsItem.getId());
		storedCacheService.del(key);

		//remove msg in special location
		key = getNewsTypeLocationKey(newsItem.getNewsType(), newsItem.getLocationCode());
		storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());

		//remove id for record(type:subtype:location)
		key = getNewsTypeSubTypeLocationKey(newsItem.getNewsType(), newsItem.getNewsSubType(), newsItem.getLocationCode());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());
	}

	public void loadNewsToRedis(NewsDTO newsItem)
	{
		if (newsItem == null)
			return;

		/*if (newsItem.getPublisherId() != null)
		{
			UserDTO userDTO = userCommonServiceUtil.getUserDTO(newsItem.getPublisherId());
			if (userDTO != null)
			{
				newsItem.setPublishSource(userDTO.getNickName());
			}
		}*/

		//cache newsItem
		storedCacheService.set(getNewsItemKey(newsItem.getId()), GsonUtil.toJson(newsItem));

		//cache msg in specific location(type:location)
		String key = getNewsTypeLocationKey(newsItem.getNewsType(), newsItem.getLocationCode());
		storedCacheService.zadd(key, Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), newsItem.getLocationCode(), key);

		//store id for record(type:subtype:location)
		key = getNewsTypeSubTypeLocationKey(newsItem.getNewsType(), newsItem.getNewsSubType(), newsItem.getLocationCode());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zadd(key, Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), newsItem.getLocationCode(), key);
	}

	protected String getSessionKey(Long adminID)
	{
		return String.format("Session:%s", adminID.toString());
	}
}
