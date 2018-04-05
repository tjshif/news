package com.school.Redis;

import com.school.Constants.EnvConst;
import com.school.Constants.LocationConst;
import com.school.DAO.INewsDao;
import com.school.Entity.NewsDTO;
import com.school.Enum.NewsEnum;
import com.school.Utils.GsonUtil;
import com.school.Utils.TimeUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class LoadDateToRedis extends RedisHandler{
	private Logger logger = Logger.getLogger(LoadDateToRedis.class.getName());

	private static final String LoadDateToRedisLock = "LoadDateToRedisLock";

	private static final String RemoveDateFromRedisLock = "RemoveDateFromRedisLock";

	@Resource
	private INewsDao newsDao;

	@Resource
	private DistributedLock distributedLock;

	public void LoadDataToRedisByDate(Integer newsType, Integer count)
	{
		//用分布式锁，timout是1个小时。
		Boolean ownTheLock = false;
		try
		{
			if (distributedLock.tryLock(LoadDateToRedisLock))
			{
				ownTheLock = true;
				if (count < 1)
					return;
				Long storedNewsIdx = getStoredIndex(newsType);
				List<NewsDTO> items = newsDao.selectNewsGreaterThanId(newsType, null, null, storedNewsIdx, count);

				for (NewsDTO item : items)
				{
					LoadNewsToRedis(item);
					storedCacheService.zadd(getNewsTypeKey(item.getNewsType()), Long.parseLong(item.getId()), item.getId());
					storedCacheService.zadd(getTrimKey(), LocationConst.ALL, getNewsTypeKey(item.getNewsType()));
				}
			}
			else
			{
				logger.warn("can't get the lock");
			}
		}
		catch (Throwable t)
		{
			logger.error(t.getMessage());
		}
		finally {
			if (ownTheLock)
				distributedLock.unlock(LoadDateToRedisLock);
		}
	}

	private void LoadNewsToRedis(NewsDTO newsItem)
	{
		if (newsItem == null)
			return;
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

	public void removeDataFromRedis()
	{
		//用分布式锁，timout是1个小时。
		Boolean ownTheLock = false;
		try {
			if (distributedLock.tryLock(RemoveDateFromRedisLock)) {
				ownTheLock = true;
				Set<Tuple> trimKeys = storedCacheService.zrangeWithScores(getTrimKey(), 0, -1);
				for (Tuple trimKey : trimKeys)
				{
					Integer location = (int)trimKey.getScore();
					if (location.intValue() == LocationConst.ALL)
					{
						trimSortedListAndNewsItem(trimKey.getElement(), EnvConst.NEWS_REDIS_COUNT_ALL_LOCATION);
					}
					else
					{
						trimSortedList(trimKey.getElement(), EnvConst.NEWS_REDIS_COUNT_ONE_LOCATION);
					}
				}
			}
		}
		catch (Throwable t)
		{
			logger.error(t.getMessage());
		}
		finally {
			if (ownTheLock)
				distributedLock.unlock(RemoveDateFromRedisLock);
		}

	}

	private void trimSortedList(String key, Integer limitSize)
	{
		Long nCount = storedCacheService.zcard(key);
		if (nCount <= limitSize)
			return;
		Long removeMaxId = nCount - limitSize - 1;
		storedCacheService.zremrangeByRank(key, 0, removeMaxId);
	}

	private void trimSortedListAndNewsItem(String key, Integer limitSize)
	{
		Long nCount = storedCacheService.zcard(key);
		if (nCount <= limitSize)
			return;

		//返回超过的Item，然后把news表中的redis删除。
		Long removeMaxIdx = nCount - limitSize - 1;
		Set<String> candidateRemoves = storedCacheService.zrange(key, 0, removeMaxIdx);
		for (String candidate : candidateRemoves)
		{
			String removeItemKey = getNewsItemKey(candidate);
			storedCacheService.del(removeItemKey);
		}
		storedCacheService.zremrangeByRank(key, 0, removeMaxIdx);
	}
}
