package com.school.Redis;

import com.school.Entity.NewsDTO;
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

	//null表示需要从db读取，
	public List<NewsDTO> getNewsSubjectList(Integer newsType, Integer subNewsType, Integer location, Integer startFrom,
											Integer count)
	{
		String key = getNewsTypeLocationKey(newsType, location);
		if (subNewsType != null)
			key = getNewsTypeSubTypeLocationKey(newsType, subNewsType, location);
		//获取最新的
		Set<String> resultIdxList = null;
		if (startFrom == null || startFrom == -1)
		{
			resultIdxList = storedCacheService.zrevrangeByScore(key, Integer.MAX_VALUE, 0, 0, count);
		}
		else
		{
			resultIdxList = storedCacheService.zrevrangeByScore(key, startFrom, 0, 0, count);
		}
		if (resultIdxList == null || resultIdxList.size() != count)
			return null;

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
}
