package com.school.Redis;

import com.school.Entity.NewsDTO;
import com.school.Enum.NewsSubTypeEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Utils.GsonUtil;
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
		String key = getNewsTypeLocationKey(newsType.getNewsTypeCode(), location);
		if (subNewsType != null)
			key = getNewsTypeSubTypeLocationKey(newsType.getNewsTypeCode(), subNewsType.getNewsSubTypeCode(), location);

		Integer offset = page * pageSize;

		return getSubjectListByOffset(key, Long.MAX_VALUE, 0, offset , pageSize);
	}

	//null表示需要从db读取，
	//不包括startfrom
	public List<NewsDTO> getNewsSubjectListLessThanId(NewsTypeEnum newsType, NewsSubTypeEnum subNewsType, Integer location, Long startFrom,
													  Integer count)
	{
		String key = getNewsTypeLocationKey(newsType.getNewsTypeCode(), location);
		if (subNewsType != null)
			key = getNewsTypeSubTypeLocationKey(newsType.getNewsTypeCode(), subNewsType.getNewsSubTypeCode(), location);
		//获取最新的
		double max = 0;
		if (startFrom == null || startFrom == -1)
		{
			max = Long.MAX_VALUE;
		}
		else
		{
			max = startFrom - 1;
		}
		return getSubjectListByOffset(key, max, 0, 0, count);
	}

	private List<NewsDTO> getSubjectListByOffset(String key, double max, double min, int offset, int count)
	{
		if (TextUtils.isEmpty(key))
			return null;

		Set<String> resultIdxList = storedCacheService.zrevrangeByScore(key, max, min, offset, count);
		if (resultIdxList == null || resultIdxList.size() != count) {
			logger.error("can't find in redis: key:" + key + "; max:" + max + "; min: " + min + "; offset:" + offset
			+ "; count: " + count);
			return null;
		}
		List<NewsDTO> newsResultDTOs = new ArrayList<>();
		for (String idx : resultIdxList)
		{
			key = getNewsItemKey(idx);
			String value = storedCacheService.get(key);
			if (TextUtils.isEmpty(value))
				return null;

			NewsDTO newsDTO = GsonUtil.fromJson(value, NewsDTO.class);
			if (newsDTO == null)
				return null;
			newsResultDTOs.add(newsDTO);
		}
		return newsResultDTOs;
	}

	public String readSession(Long adminID)
	{
		String key = getSessionKey(adminID);
		return storedCacheService.get(key);
	}
}
