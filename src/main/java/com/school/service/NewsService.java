package com.school.service;

import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IFavoriteNewsDao;
import com.school.DAO.INewsDao;
import com.school.DAO.INewsDetailDao;
import com.school.DAO.IUserDao;
import com.school.Entity.*;
import com.school.Enum.NewsSubTypeEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Gson.*;
import com.school.Redis.ReadDataFromRedis;
import com.school.service.common.CommentsServiceUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

	@Resource
	private CommentsServiceUtils commentsServiceUtils;

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

	public NewsSubjectResultGson getNewsSubjectListByPage(NewsTypeEnum newsType, NewsSubTypeEnum newsSubType, Integer location,
														  Integer page, Integer pageSize)
	{
		NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		List<NewsDTO> newsDTOList = readDataFromRedis.getNewsSubjectListByPage(newsType, newsSubType, location, page, pageSize);
		if (newsDTOList == null)
		{
			Long offset = (long)page * pageSize;
			newsDTOList = newsDao.selectNewsByPage(newsType.getNewsTypeCode(), newsSubType != null ? newsSubType.getNewsSubTypeCode() : null,
					location, offset, pageSize);
		}
		appendCommentCount(newsDTOList);
		resultGson.setNewsList(newsDTOList);
		return resultGson;
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
		appendCommentCount(newsDTOList);
		resultGson.setNewsList(newsDTOList);
		return resultGson;
	}

	private void appendCommentCount(List<NewsDTO> newsDTOList)
	{
		List<String> newsIDs = new ArrayList<>();
		for (NewsDTO newsDTO : newsDTOList)
		{
			if (newsDTO == null)
				continue;
			newsIDs.add(newsDTO.getId());
		}

		if (newsIDs.size() > 0)
		{
			List<CommentCountDTO> commentCountDTOS = commentsServiceUtils.selectCommentCounts(newsIDs);

			for (NewsDTO newsDTO : newsDTOList)
			{
				Iterator<CommentCountDTO> it = commentCountDTOS.iterator();
				while(it.hasNext()){
					CommentCountDTO commentCountDTO = it.next();
					if(commentCountDTO.getNewsID().equalsIgnoreCase(newsDTO.getId())){
						newsDTO.setCommentCount(commentCountDTO.getTotalCount());
						it.remove();
						break;
					}
				}
			}
		}
	}

	public NewsDetailResultGson getNewsDetail(Long newsID, Long userID)
	{
		NewsDetailResultGson newsDetailResultGson = new NewsDetailResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			NewsDetailDTO newsDetailDTO = newsDetailDao.selectNewsDetail(newsID);
			if (newsDetailDTO == null) {
				newsDetailDTO = new NewsDetailDTO();
				newsDetailDTO.setDetailContent("<b>你来晚了，改主题已经被删除了！</b>");
				newsDetailResultGson.setNewsDetailDTO(newsDetailDTO);
				return newsDetailResultGson;
			}
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

	@Transactional
	public RetResultGson updateNewsValid(Long newsID, Boolean isValid)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			int row = newsDao.updateNewsStatus(newsID, isValid);
			if (row != 1)
			{
				logger.error("failed to find row : ID = " + newsID);
				resultGson.setResult(RetCode.RET_ERROR_FIND_ROW, RetMsg.RET_MSG_FIND_ROW);
				return resultGson;
			}
			NewsDTO newsDTO = newsDao.selectNewsById(newsID);
			if (isValid)
			{
				readDataFromRedis.LoadNewsToRedis(newsDTO);
			}
			else
			{
				readDataFromRedis.removeNewsFromRedis(newsDTO);
			}

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

	public NewsSubjectResultGson getFavoriteNews(Long userID, Integer page, Integer pageSize)
	{
		NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			Integer offset = page * pageSize;
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

	public NewsCountResultGson getNewsCount(NewsTypeEnum newsType, NewsSubTypeEnum newsSubType, Integer location)
	{
		NewsCountResultGson resultGson =  new NewsCountResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			Integer count = newsDao.getCount(newsType.getNewsTypeCode(), newsSubType != null ? newsSubType.getNewsSubTypeCode() : null,
					location);
			resultGson.setCount(count);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

}
