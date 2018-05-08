package com.school.Redis;

import com.school.DAO.IUserDao;
import com.school.Entity.NewsDTO;
import com.school.Entity.UserDTO;
import com.school.Enum.LocationEnum;
import com.school.Utils.GsonUtil;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class RedisHandler {
	private Logger logger = Logger.getLogger(RedisHandler.class.getName());

	@Resource
	private IUserDao userDao;

	@Resource
	protected StoredCacheService storedCacheService;

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

	protected String getNewsTypeKey(Integer newsType)
	{
		return String.format("type:%d;location:%d", newsType, LocationEnum.ALL.getZipCode());
	}

	protected String getTrimKey()
	{
		return "TRIMKEYS";
	}

	protected Long getStoredIndex(Integer newsType)
	{
		Long storedNewsIdx = -1L;
		String newsKey = getNewsTypeKey(newsType);
		if (storedCacheService.exists(newsKey))
		{
			Set<String> latestElem = storedCacheService.zrevrange(newsKey, 0, 0);
			if (latestElem != null && latestElem.size() == 1)
			{
				for (String item : latestElem)
				{
					storedNewsIdx = Long.parseLong(item);
					break;
				}
			}
		}
		return storedNewsIdx;
	}

	public void removeNewsFromRedis(NewsDTO newsItem)
	{
		if (newsItem == null)
			return;
		String key = getNewsTypeKey(newsItem.getNewsType());
		if (!TextUtils.isEmpty(key))
			storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());

		key = getNewsTypeLocationKey(newsItem.getNewsType(), newsItem.getLocation());
		storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());

		key = getNewsItemKey(newsItem.getId());
		storedCacheService.del(key);

		key = getNewsTypeSubTypeLocationKey(newsItem.getNewsType(), newsItem.getNewsSubType(), newsItem.getLocation());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());
	}

	public void LoadNewsToRedis(NewsDTO newsItem)
	{
		if (newsItem == null)
			return;
		storedCacheService.zadd(getNewsTypeKey(newsItem.getNewsType()), Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), LocationEnum.ALL.getZipCode(), getNewsTypeKey(newsItem.getNewsType()));

		if (newsItem.getPublisherId() != null)
		{
			UserDTO userDTO = userDao.selectByID(newsItem.getPublisherId());
			if (userDTO != null)
			{
				newsItem.setPublishSource(userDTO.getNickName());
			}
		}

		storedCacheService.set(getNewsItemKey(newsItem.getId()), GsonUtil.toJson(newsItem));

		String key = getNewsTypeLocationKey(newsItem.getNewsType(), newsItem.getLocation());
		if (TextUtils.isEmpty(key))
		{
			logger.error("location can't be null.");
			return;
		}

		storedCacheService.zadd(key, Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), newsItem.getLocation(), key);

		//store id for record(type:subtype:location)
		key = getNewsTypeSubTypeLocationKey(newsItem.getNewsType(), newsItem.getNewsSubType(), newsItem.getLocation());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zadd(key, Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), newsItem.getLocation(), key);
	}

	protected String getSessionKey(Long adminID)
	{
		return String.format("Session:%s", adminID.toString());
	}
}
