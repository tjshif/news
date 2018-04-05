package com.school.Redis;

import com.school.Enum.LocationEnum;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.Set;

public class RedisHandler {
	private Logger logger = Logger.getLogger(RedisHandler.class.getName());

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
}
