package com.school.service.common;

import com.school.AOP.CacheMethodLogo;
import com.school.DAO.INewsDao;
import com.school.Entity.NewsDTO;
import com.school.Utils.TimeUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NewsServiceUtils {
	@Resource
	private INewsDao newsDao;

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS)
	public NewsDTO getNews(Long newsID)
	{
		if (newsID == null)
			return null;

		return newsDao.selectNewsById(newsID);
	}
}
