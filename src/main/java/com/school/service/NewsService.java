package com.school.service;

import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.INewsDao;
import com.school.DAO.INewsDetailDao;
import com.school.Entity.NewsDTO;
import com.school.Entity.NewsDetailDTO;
import com.school.Enum.NewsSubTypeEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Gson.NewsDetailResultGson;
import com.school.Gson.NewsSubjectResultGson;
import com.school.Redis.ReadDataFromRedis;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class NewsService {
	private Logger logger = Logger.getLogger(NewsService.class.getName());

	@Resource
	private ReadDataFromRedis readDataFromRedis;

	@Resource
	private INewsDao newsDao;

	@Resource
	private INewsDetailDao newsDetailDao;

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

	public NewsSubjectResultGson getNewsSubjectList(NewsTypeEnum newsType, NewsSubTypeEnum newsSubType, Integer location,
													Long startFrom, Integer count)
	{
		NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);

		List<NewsDTO> newsDTOList = readDataFromRedis.getNewsSubjectListLessThanId(newsType, newsSubType, location, startFrom, count);

		if (newsDTOList == null)//表示redis里面没有取到数据
		{//data from db
			newsDTOList = newsDao.selectNewsLessThanId(newsType.getNewsTypeCode(), newsSubType != null ? newsSubType.getNewsSubTypeCode() : null,
					location, startFrom, count);
		}
		resultGson.setNewsList(newsDTOList);
		return resultGson;
	}

	public NewsDetailResultGson getNewsDetail(Long newsID)
	{
		NewsDetailResultGson newsDetailResultGson = new NewsDetailResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			NewsDetailDTO newsDetailDTO = newsDetailDao.selectNewsDetail(newsID);
			newsDetailResultGson.setNewsDetailDTO(newsDetailDTO);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			newsDetailResultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return newsDetailResultGson;
	}
}
