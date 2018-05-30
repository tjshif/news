package com.school.Redis;

import com.school.DAO.IUserDao;
import com.school.Entity.BaseDTO;
import com.school.Entity.NewsDTO;
import com.school.Utils.GsonUtil;
import com.school.service.common.UserCommonServiceUtil;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RedisHandler {
	private final Logger logger = Logger.getLogger(getClass().getName());

	@Resource
	protected StoredCacheService storedCacheService;

	@Resource
	private UserCommonServiceUtil userCommonServiceUtil;

	protected String getTrimKey()
	{
		return "TRIMKEYS";
	}

	public void removeNewsFromRedis(NewsDTO newsItem)
	{
		if (newsItem == null)
			return;

		//remove msgitem
		String key = NewsDTO.getNewsItemKey(newsItem.getId());
		storedCacheService.del(key);

		//remove msg in special location
		key = NewsDTO.getNewsTypeLocationKey(newsItem.getNewsType(), newsItem.getLocationCode());
		storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());

		//remove id for record(type:subtype:location)
		key = NewsDTO.getNewsTypeSubTypeLocationKey(newsItem.getNewsType(), newsItem.getNewsSubType(), newsItem.getLocationCode());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());
	}

	public void loadNewsToRedis(NewsDTO newsItem)
	{
		if (newsItem == null)
			return;

		//cache newsItem
		storedCacheService.set(NewsDTO.getNewsItemKey(newsItem.getId()), GsonUtil.toJson(newsItem));

		//cache msg in specific location(type:location)
		String key = NewsDTO.getNewsTypeLocationKey(newsItem.getNewsType(), newsItem.getLocationCode());
		storedCacheService.zadd(key, Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), newsItem.getLocationCode(), key);

		//store id for record(type:subtype:location)
		key = NewsDTO.getNewsTypeSubTypeLocationKey(newsItem.getNewsType(), newsItem.getNewsSubType(), newsItem.getLocationCode());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zadd(key, Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), newsItem.getLocationCode(), key);
	}

	public <T extends BaseDTO> String loadItemToRedis(T item, int seconds)
	{
		if (item == null || seconds < 1)
			return null;

		return storedCacheService.setex(item.getKey(item.getId()), seconds, GsonUtil.toJson(item));
	}

	public <T extends BaseDTO> List<String> loadItemsToRedis(List<T> items, int seconds)
	{
		if (items == null || items.size() == 0)
			return null;

		List<String> result = new ArrayList<>();
		for (T item : items)
		{
			String itemResult = loadItemToRedis(item, seconds);
			result.add(itemResult);
		}
		return result;
	}

	public <T extends BaseDTO> T readItemFromRedis(String key, Class<T> clazz)
	{
		if (TextUtils.isEmpty(key))
			return null;

		String gsonValue = storedCacheService.get(key);
		if (TextUtils.isEmpty(gsonValue))
			return null;

		return GsonUtil.fromJson(gsonValue, clazz);
	}

	public <T extends BaseDTO> Set<T> readItemsFromRedisByIDs(Set<String> IDs, Class<T> clazz, Set<String> notExistPublishers)
	{
		if (IDs == null || IDs.size() == 0)
			return null;

		Set<T> DTOs = new HashSet<>();
		for (String ID : IDs)
		{
			T publisher;
			try {
				publisher = clazz.newInstance();
			} catch (Exception ex) {
				logger.error("", ex);
				continue;
			}
			publisher = readItemFromRedis(publisher.getKey(ID), clazz);
			if (publisher == null)
				notExistPublishers.add(ID);
			else
				DTOs.add(publisher);
		}
		return DTOs;
	}

	protected String getSessionKey(Long adminID)
	{
		return String.format("Session:%s", adminID.toString());
	}
}
