package com.school.Redis;

import com.school.DAO.INewsDao;
import com.school.Entity.NewsDTO;
import com.school.Enum.NewsEnum;
import com.school.Utils.GsonUtil;
import com.school.Utils.TimeUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class LoadDateToRedis {
	@Resource
	private StoredCacheService storedCacheService;

	@Resource
	private INewsDao newsDao;

	public void LoadDataToRedisByDate(Date date, NewsEnum newsEnum)
	{
		String strDay = TimeUtils.getDay(date);
		if (TextUtils.isEmpty(strDay))
			return;

		String dayNewsEnumKey = String.format("%s:%d", strDay, newsEnum.getNewsType());
		if(storedCacheService.exists(dayNewsEnumKey))
			return;
		List<NewsDTO> newsDtoList = newsDao.selectNewsByCreateAt(date);
		for (NewsDTO newsDTO: newsDtoList)
		{
			storedCacheService.zadd(dayNewsEnumKey, Long.parseLong(newsDTO.getId()), GsonUtil.toJson(newsDTO));
		}

	}
}
