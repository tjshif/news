package com.school.service;

import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IFavoriteNewsDao;
import com.school.DAO.INewsDao;
import com.school.DAO.INewsDetailDao;
import com.school.DAO.IUserDao;
import com.school.Entity.FavoriteNewsDTO;
import com.school.Entity.NewsDTO;
import com.school.Entity.NewsDetailDTO;
import com.school.Entity.UserDTO;
import com.school.Enum.NewsSubTypeEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Gson.NewsDetailResultGson;
import com.school.Gson.NewsFavoriteResultGson;
import com.school.Gson.NewsSubjectResultGson;
import com.school.Gson.RetResultGson;
import com.school.Redis.ReadDataFromRedis;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private IFavoriteNewsDao favoriteNewsDao;

	@Resource
	private INewsDetailDao newsDetailDao;

	@Resource
	private IUserDao userDao;

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

	public NewsDetailResultGson getNewsDetail(Long newsID, Long userID)
	{
		NewsDetailResultGson newsDetailResultGson = new NewsDetailResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			NewsDetailDTO newsDetailDTO = newsDetailDao.selectNewsDetail(newsID);
			if (newsDetailDTO == null)
				return newsDetailResultGson;
			newsDetailDTO.setFavorite(false);
			if (userID != null)
			{
				FavoriteNewsDTO favoriteNewsDTO = new FavoriteNewsDTO(userID, newsID);
				favoriteNewsDTO = favoriteNewsDao.selectByUK(favoriteNewsDTO);
				if (favoriteNewsDTO != null)
					newsDetailDTO.setFavorite(true);
			}

			UserDTO userDTO = userDao.selectByID(newsDetailDTO.getPublisher_id());
			if (userDTO != null)
			{
				newsDetailDTO.setPublisher_avatar_url(userDTO.getAvatarUrl());
				newsDetailDTO.setPublisher_name(userDTO.getNickName());
				newsDetailDTO.setPublisher_id(newsDetailDTO.getPublisher_id());
			}
			newsDetailResultGson.setNewsDetailDTO(newsDetailDTO);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			newsDetailResultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return newsDetailResultGson;
	}

	@Transactional
	public NewsFavoriteResultGson addOrDeleteFavoriteNews(Boolean bAdd, Long userID, Long newsID)
	{
		NewsFavoriteResultGson resultGson = new NewsFavoriteResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			FavoriteNewsDTO favoriteNewsDTO = new FavoriteNewsDTO(userID, newsID);
			if (bAdd)
				favoriteNewsDao.insert(favoriteNewsDTO);
			else
				favoriteNewsDao.delete(favoriteNewsDTO);
			resultGson.setFavorite(bAdd);
		}
		catch (DuplicateKeyException ex)
		{
			resultGson.setFavorite(true);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	@Transactional
	public NewsFavoriteResultGson clearFav(Long userID)
	{
		NewsFavoriteResultGson resultGson = new NewsFavoriteResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			favoriteNewsDao.clear(userID);
			resultGson.setFavorite(false);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public NewsFavoriteResultGson getIsFavorite(Long newsID, Long userID)
	{
		NewsFavoriteResultGson resultGson = new NewsFavoriteResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		resultGson.setFavorite(false);
		if (userID == null)
			return resultGson;

		try
		{
			FavoriteNewsDTO favoriteNewsDTO = new FavoriteNewsDTO(userID, newsID);
			favoriteNewsDTO = favoriteNewsDao.selectByUK(favoriteNewsDTO);
			if (favoriteNewsDTO != null)
				resultGson.setFavorite(true);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public NewsSubjectResultGson getFavoriteNews(Long userID, Long offset, Integer pageSize)
	{
		NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			List<NewsDTO> newsDTOs = favoriteNewsDao.selectNewsByUserID(userID, offset, pageSize);
			resultGson.setNewsList(newsDTOs);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

}
