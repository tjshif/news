package com.school.Redis;

import com.school.Constants.EnvConst;
import com.school.DAO.INewsDao;
import com.school.Entity.NewsDTO;
import com.school.Enum.NewsTypeEnum;
import com.school.Utils.AppProperites;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class LoadDataToRedis extends RedisHandler{
	private Logger logger = Logger.getLogger(LoadDataToRedis.class.getName());

	private static final String LoadDateToRedisLock = "LoadDateToRedisLock";

	private static final String RemoveDateFromRedisLock = "RemoveDateFromRedisLock";

	@Resource
	private INewsDao newsDao;

	@Resource
	private DistributedLock distributedLock;

	@Scheduled(cron = "0 0/30 * * * ?")
	public void LoadDataToRedis()
	{
		Date date = new Date();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sim.format(date);
		logger.info("sheduler job: " + dateStr);

		NewsTypeEnum[] enums = NewsTypeEnum.values();
		for (int ii = 0; ii < enums.length; ++ii)
		{
			loadTopCountDataToRedis(enums[ii].getNewsTypeCode(), appProperites.getPage_size());
		}
	}

	public void loadTopCountDataToRedis(Integer newsType, Integer count)
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

				List<NewsDTO> items = newsDao.selectNewsLessThanId(newsType, null, null, null, count);
				for (NewsDTO item : items)
				{
					loadNewsToRedis(item);
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

	@Scheduled(cron = "0 10/30 * * * ?")
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
					trimSortedList(trimKey.getElement(), EnvConst.NEWS_REDIS_COUNT_ONE_LOCATION);
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

	public void LoadSession(Long adminID, int seconds, String sessionID)
	{
		String key = getSessionKey(adminID);
		storedCacheService.setex(key, seconds, sessionID);
	}
}
