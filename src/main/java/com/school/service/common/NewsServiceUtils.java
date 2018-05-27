package com.school.service.common;

import com.school.AOP.CacheMethodLogo;
import com.school.DAO.IFavoriteNewsDao;
import com.school.DAO.INewsDao;
import com.school.DAO.INewsDetailDao;
import com.school.DAO.IVisitCountDao;
import com.school.Entity.FavoriteNewsDTO;
import com.school.Entity.MsgAggregate;
import com.school.Entity.NewsDTO;
import com.school.Entity.NewsDetailDTO;
import com.school.Gson.CounterMsgGson;
import com.school.Gson.MsgGson;
import com.school.Redis.ReadDataFromRedis;
import com.school.Utils.TimeUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
public class NewsServiceUtils {
	@Resource
	private INewsDao newsDao;

	@Resource
	private INewsDetailDao newsDetailDao;

	@Resource
	private IVisitCountDao visitCountDao;

	@Resource
	private IFavoriteNewsDao favoriteNewsDao;

	@Resource
	private ReadDataFromRedis readDataFromRedis;
	
	@Transactional
	public void saveMsgToDB(MsgAggregate msgAggregate)
	{
		if (msgAggregate.getNewsDTO() != null)
			newsDao.insert(msgAggregate.getNewsDTO());

		if (msgAggregate.getNewsDetailDTO() != null)
			newsDetailDao.insert(msgAggregate.getNewsDetailDTO());
	}

	@Transactional
	public void increaseVisitCount(CounterMsgGson counterMsgGson)
	{
		if (counterMsgGson == null)
			return;
		visitCountDao.increase(counterMsgGson.getNewsID());
	}

	public NewsDTO getNewsByID(Long newsID)
	{
		NewsDTO newsDTO = readDataFromRedis.getNewsDTO(newsID);
		if (newsDTO == null)
		{
			newsDTO = newsDao.selectNewsById(newsID);
		}
		return newsDTO;
	}

	public NewsDetailDTO getNewsDetailByNewsID(Long newsID)
	{
		return newsDetailDao.selectNewsDetail(newsID);
	}

	public Boolean isFavoriteNews(Long userID, Long newsID)
	{
		Boolean bFav = false;

		if (userID != null)
		{
			FavoriteNewsDTO favoriteNewsDTO = new FavoriteNewsDTO(userID, newsID);
			favoriteNewsDTO = favoriteNewsDao.selectByUK(favoriteNewsDTO);
			if (favoriteNewsDTO != null)
				bFav = true;
		}
		return bFav;
	}
}
