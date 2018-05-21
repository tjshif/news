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
import com.school.Utils.ConvertUtils;
import com.school.service.common.BeAdminServiceUtils;
import com.school.service.common.CommentsServiceUtils;
import com.school.service.common.UserCommonServiceUtil;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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

	@Resource
	private UserCommonServiceUtil userCommonServiceUtil;

	@Resource
	private BeAdminServiceUtils beAdminServiceUtils;

	public NewsSubjectResultGson getNewsSubjectListByPage(NewsTypeEnum newsType, NewsSubTypeEnum newsSubType, Integer location,
														  Integer page, Integer pageSize)
	{
		NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			List<NewsDTO> newsDTOList = readDataFromRedis.getNewsMsgListByPage(newsType, newsSubType, location, page, pageSize);
			if (newsDTOList == null)
			{
				Long offset = (long)page * pageSize;
				newsDTOList = newsDao.selectNewsByPage(newsType.getNewsTypeCode(), newsSubType != null ? newsSubType.getNewsSubTypeCode() : null,
						location, offset, pageSize);
			}
			List<MsgGson> msgGsons = ConvertUtils.convertToMsgGsonList(newsDTOList);
			appendCommentCount(msgGsons);
			appendPublishInfo(msgGsons);
			resultGson.setMsgGsonList(msgGsons);
		}
		catch (Exception ex)
		{
			logger.error("",ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public NewsSubjectResultGson getNewsSubjectList(NewsTypeEnum newsType, NewsSubTypeEnum newsSubType, Integer location,
													Long startFrom, Integer count)
	{
		NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);

		try {
			List<NewsDTO> newsDTOList = readDataFromRedis.getNewsSubjectListLessThanId(newsType, newsSubType, location, startFrom, count);

			if (newsDTOList == null)//表示redis里面没有取到数据
			{//data from db
				newsDTOList = newsDao.selectNewsLessThanId(newsType.getNewsTypeCode(), newsSubType != null ? newsSubType.getNewsSubTypeCode() : null,
						location, startFrom, count);
			}
			List<MsgGson> msgGsons = ConvertUtils.convertToMsgGsonList(newsDTOList);
			appendCommentCount(msgGsons);
			appendPublishInfo(msgGsons);
			resultGson.setMsgGsonList(msgGsons);
		}
		catch (Exception ex)
		{
			logger.error("",ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	private void appendPublishInfo(List<MsgGson> msgGsonList)
	{
		Set<String> publishIDs = new HashSet<>();
		for (MsgGson msgGson : msgGsonList)
		{
			if (msgGson == null)
				continue;
			publishIDs.add(msgGson.getPublisherId().toString());
		}

		if (publishIDs.size() > 0)
		{
			List<UserDTO> userDTOS = userCommonServiceUtil.selectUsers(publishIDs);
			if (userDTOS == null)
				return;
			for (MsgGson msgGson : msgGsonList)
			{
				for (UserDTO userDTO : userDTOS)
				{
					if (msgGson.getPublisherId().toString().equalsIgnoreCase(userDTO.getId()))
					{
						msgGson.setPublishSource(userDTO.getNickName());
						msgGson.setPublishAvatar(userDTO.getAvatarUrl());
						break;
					}
				}
			}
		}
	}

	private void appendCommentCount(List<MsgGson> msgGsonsList)
	{
		List<String> msgIDs = new ArrayList<>();
		for (MsgGson msgGson : msgGsonsList)
		{
			if (msgGson == null)
				continue;
			msgIDs.add(msgGson.getID());
		}

		if (msgIDs.size() > 0)
		{
			List<CommentCountDTO> commentCountDTOS = commentsServiceUtils.selectCommentCounts(msgIDs);
			if (commentCountDTOS == null)
				return;
			for (MsgGson msgGson : msgGsonsList)
			{
				Iterator<CommentCountDTO> it = commentCountDTOS.iterator();
				while(it.hasNext()){
					CommentCountDTO commentCountDTO = it.next();
					if(commentCountDTO.getNewsID().equalsIgnoreCase(msgGson.getID())){
						msgGson.setCommentCount(commentCountDTO.getTotalCount());
						it.remove();
						break;
					}
				}
			}
		}
	}

	public RetIDResultGson getNewsDetailID(String linkUrl)
	{
		RetIDResultGson resultGson = new RetIDResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			Long id = newsDetailDao.selectNewsDetailIDByUrl(linkUrl);
			resultGson.setID(id);
		}
		catch (Exception ex)
		{
			logger.error("",ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
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

			UserDTO userDTO = userDao.selectByID(newsDetailDTO.getPublisher_id().toString());
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

			loadOrRemoveNewsInRedis(newsID, isValid);
		}
		catch (Exception ex)
		{
			logger.error("", ex);
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
			logger.error("", ex);
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
			List<MsgGson> msgGsons = ConvertUtils.convertToMsgGsonList(newsDTOs);
			appendPublishInfo(msgGsons);
			appendCommentCount(msgGsons);
			resultGson.setMsgGsonList(msgGsons);
		}
		catch (Exception ex)
		{
			logger.error("", ex);
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

	public RetResultGson updateNewsSubject(String sessionID, NewsGson newsGson)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		if (!beAdminServiceUtils.isLoginIn(newsGson.getID(), sessionID))
		{
			resultGson.setResult(RetCode.RET_ERROR_BEADMIN_SESSION_OUTDATE, RetMsg.RET_MSG_BEADMIN_SESSION_OUTDATE);
			return resultGson;
		}

		try {
			Integer nRow = newsDao.update(newsGson);
			if (nRow != 1) {
				resultGson.setResult(RetCode.RET_ERROR_INVALID_USERID, RetMsg.RET_MSG_INVALID_USERID);
				return resultGson;
			}
			loadOrRemoveNewsInRedis(newsGson.getID(), true);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public RetResultGson updateNewsDetail(String sessionID, NewsDetailGson newsDetailGson)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		if (!beAdminServiceUtils.isLoginIn(newsDetailGson.getNewsID(), sessionID))
		{
			resultGson.setResult(RetCode.RET_ERROR_BEADMIN_SESSION_OUTDATE, RetMsg.RET_MSG_BEADMIN_SESSION_OUTDATE);
			return resultGson;
		}
		try {
			Integer nRow = newsDetailDao.update(newsDetailGson);
			if (nRow != 1) {
				resultGson.setResult(RetCode.RET_ERROR_INVALID_USERID, RetMsg.RET_MSG_INVALID_USERID);
				return resultGson;
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	private void loadOrRemoveNewsInRedis(Long newsID, Boolean isLoad)
	{
		NewsDTO newsDTO = newsDao.selectNewsById(newsID);
		if (isLoad)
		{
			readDataFromRedis.loadNewsToRedis(newsDTO);
		}
		else
		{
			readDataFromRedis.removeNewsFromRedis(newsDTO);
		}
	}

}
