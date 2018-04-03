package com.school.service;

import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.INewsDao;
import com.school.Entity.NewsDTO;
import com.school.Gson.NewsSubjectResultGson;
import com.school.Redis.ReadDataFromRedis;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class NewsService {
	Logger logger = Logger.getLogger(NewsService.class.getName());

	@Resource
	private ReadDataFromRedis readDataFromRedis;

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

	public List<NewsDTO> selectNewsByCreateAtFromID(Date date, Long fromID)
	{
		List<NewsDTO> news = null;
		try {
			news = newsDao.selectNewsByCreateAtFromID(date, fromID);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return news;
	}

	public NewsSubjectResultGson getNewsSubjectList(Integer newsType, Integer subNewsType, Integer location,
													Integer startFrom, Integer count)
	{
		NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);

		List<NewsDTO> newsDTOList = readDataFromRedis.getNewsSubjectList(newsType, subNewsType, location, startFrom, count);

		if (newsDTOList != null)
			resultGson.setNewsList(newsDTOList);
		return resultGson;
	}
}
