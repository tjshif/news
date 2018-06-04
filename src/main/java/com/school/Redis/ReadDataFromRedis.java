package com.school.Redis;

import com.school.Entity.NewsDTO;
import com.school.Enum.NewsSubTypeEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Enum.TagEnum;
import com.school.Utils.GsonUtil;
import com.school.Utils.RandUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ReadDataFromRedis extends RedisHandler {
	private Logger logger = Logger.getLogger(ReadDataFromRedis.class.getName());

	public List<NewsDTO> getNewsMsgListByPage(NewsTypeEnum newsType, NewsSubTypeEnum subNewsType, Integer location, Integer page,
												  Integer pageSize)
	{
		String key = NewsDTO.getNewsTypeLocationKey(newsType.getNewsTypeCode(), location);
		if (subNewsType != null)
			key = NewsDTO.getNewsTypeSubTypeLocationKey(newsType.getNewsTypeCode(), subNewsType.getNewsSubTypeCode(), location);

		Integer offset = page * pageSize;

		String adKey = NewsDTO.getNewsTypeTagKey(newsType.getNewsTypeCode(), TagEnum.AD_TAG.getTagName());
		String topKey = NewsDTO.getNewsTypeTagKey(newsType.getNewsTypeCode(), TagEnum.TOP_TAG.getTagName());
		Boolean isFirstPage = (page == 0 ? true : false);
		return getSubjectListByOffset(key, adKey, topKey, Long.MAX_VALUE, 0L, offset, pageSize, isFirstPage);
	}

	//null表示需要从db读取，
	//不包括startfrom
	public List<NewsDTO> getNewsSubjectListLessThanId(NewsTypeEnum newsType, NewsSubTypeEnum subNewsType, Integer location, Long startFrom,
													  Integer count)
	{
		String key = NewsDTO.getNewsTypeLocationKey(newsType.getNewsTypeCode(), location);
		if (subNewsType != null)
			key = NewsDTO.getNewsTypeSubTypeLocationKey(newsType.getNewsTypeCode(), subNewsType.getNewsSubTypeCode(), location);
		//获取最新的
		Long max = 0L;
		Boolean isFirstPage = false;
		if (startFrom == null || startFrom == -1)
		{
			max = Long.MAX_VALUE;
			isFirstPage = true;
		}
		else
		{
			max = startFrom - 1;
		}
		String adKey = NewsDTO.getNewsTypeTagKey(newsType.getNewsTypeCode(), TagEnum.AD_TAG.getTagName());
		String topKey = NewsDTO.getNewsTypeTagKey(newsType.getNewsTypeCode(), TagEnum.TOP_TAG.getTagName());
		return getSubjectListByOffset(key, adKey, topKey, max, 0L, 0, count, isFirstPage);
	}

	public NewsDTO getNewsDTO(Long newsID)
	{
		String key = NewsDTO.getNewsItemKey(newsID.toString());
		return readItemFromRedis(key, NewsDTO.class);
	}

	public List<NewsDTO> getSubjectListByOffset(String key, String adKey, String topKey, Long max, Long min,
												int offset, int count, Boolean isFirstPage)
	{
		if (TextUtils.isEmpty(key))
			return null;

		Set<String> resultIdxSet = storedCacheService.zrevrangeByScore(key, max, min, offset, count);
		if (resultIdxSet == null || resultIdxSet.size() != count) {
			logger.warn("can't find in redis: key:" + key + "; max:" + max + "; min: " + min + "; offset:" + offset
			+ "; count: " + count);
			return null;
		}

		List<String> resultIdxList = new ArrayList<>(resultIdxSet);

		if (isFirstPage) // only need to add ad,top news at first
		{
			//ad
			String adItem = getRandomItem(adKey, appProperites.getAd_percent());
			if (adItem != null && resultIdxList.size() > 2)
				resultIdxList.add(2, adItem);

			//top
			List<String> topItems = getTopItems(topKey);
			if (topItems != null && topItems.size() > 0)
				resultIdxList.addAll(0, topItems);
		}

		List<NewsDTO> newsResultDTOs = new ArrayList<>();
		for (String idx : resultIdxList)
		{
			NewsDTO newsDTO = readItemFromRedis(NewsDTO.getNewsItemKey(idx), NewsDTO.class);
			if (newsDTO == null)
				return null;
			newsResultDTOs.add(newsDTO);
		}
		return newsResultDTOs;
	}

	private List<String> getTopItems(String key)
	{
		if (TextUtils.isEmpty(key))
			return null;

		Set<String> topItems = storedCacheService.zrevrange(key, 0, -1);
		return new ArrayList<>(topItems);
	}

	private String getRandomItem(String key, Integer percent)
	{
		Boolean isPickIt = RandUtils.isRandPick(percent);
		if (!isPickIt)
			return null;

		if (TextUtils.isEmpty(key))
			return null;

		Long count = storedCacheService.zcard(key);
		if (count == 0)
			return null;

		Long index = RandUtils.random(count);
		if (index == null)
			return null;

		Set<String> result = storedCacheService.zrange(key, index, index);
		if (result.isEmpty())
			return null;
		return result.iterator().next();
	}

	public String readSession(Long adminID)
	{
		String key = getSessionKey(adminID);
		return storedCacheService.get(key);
	}
}
