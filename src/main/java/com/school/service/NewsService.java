package com.school.service;

import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.INewsDao;
import com.school.Entity.NewsDTO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class NewsService {
	Logger logger = Logger.getLogger(NewsService.class.getName());

	@Resource
	private INewsDao newsDao;

	public List<NewsDTO> selectNewsByCreateAt(Date date)
	{
		List<NewsDTO> news = null;
		try {
			news = newsDao.selectNewsByCreateAt(date);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return news;
	}
}
