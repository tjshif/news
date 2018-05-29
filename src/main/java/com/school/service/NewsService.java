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
import com.school.Utils.MessageUtils;
import com.school.service.common.BeAdminServiceUtils;
import com.school.service.common.CommentsServiceUtils;
import com.school.service.common.NewsServiceUtils;
import com.school.service.common.UserCommonServiceUtil;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.css.Counter;

import javax.annotation.Resource;
import javax.ws.rs.QueryParam;
import java.util.*;

@Service
public class NewsService {
	private Logger logger = Logger.getLogger(NewsService.class.getName());

	@Resource
	private ReadDataFromRedis readDataFromRedis;

	@Resource
	private INewsDao newsDao;

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

	@Resource
	private NewsServiceUtils newsServiceUtils;

	@Resource
	private IFavoriteNewsDao favoriteNewsDao;

	@Resource
	private MessageUtils messageUtils;

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

	public NewsSubjectResultGson getPostNews(Long userID, Integer page, Integer pageSize)
	{
		NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			Long offset = (long) page * pageSize;
			List<NewsDTO> newsDTOList = newsDao.selectPostNewsByPage(userID.toString(), offset, pageSize);
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

	public NewsDetailResultGson getNewsDetailByUrl(String linkUrl)
	{
		NewsDetailResultGson resultGson = new NewsDetailResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			NewsDetailDTO newsDetailDTO = newsDetailDao.selectNewsDetailByUrl(linkUrl);
			resultGson.setNewsDetailDTO(newsDetailDTO);
		}
		catch (Exception ex)
		{
			logger.error("",ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}

	public NewsSubjectResultGson getNewsByID(Long newsID, Long userID)
	{
		NewsSubjectResultGson newsSubjectResultGson = new NewsSubjectResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			NewsDTO newsDTO = newsServiceUtils.getNewsByID(newsID);
			if (newsDTO == null)
			{
				newsSubjectResultGson.setResult(RetCode.RET_ERROR_FIND_ROW, RetMsg.RET_MSG_FIND_ROW);
				return newsSubjectResultGson;
			}
			MsgGson msgGson = ConvertUtils.convertToMsgGson(newsDTO);
			if (newsDTO.getHasDetail())
			{
				NewsDetailDTO newsDetailDTO = newsServiceUtils.getNewsDetailByNewsID(newsID);
				if (newsDetailDTO == null)
				{
					newsSubjectResultGson.setResult(RetCode.RET_ERROR_FIND_ROW, RetMsg.RET_MSG_FIND_ROW);
					return newsSubjectResultGson;
				}
				msgGson.setDetailContent(newsDetailDTO.getDetailContent());
			}
			if (TextUtils.isEmpty(msgGson.getPublishSource()))
			{
				UserDTO userDTO = userDao.selectByID(msgGson.getPublisherId().toString());
				if (userDTO != null)
				{
					msgGson.setPublishSource(userDTO.getNickName());
					msgGson.setPublishAvatar(userDTO.getAvatarUrl());
				}
			}
			Boolean bIsFav = newsServiceUtils.isFavoriteNews(userID, newsID);
			msgGson.setFavorite(bIsFav);
			List<MsgGson> msgGsons = new ArrayList<>();
			msgGsons.add(msgGson);
			newsSubjectResultGson.setMsgGsonList(msgGsons);
		}
		catch (Exception ex)
		{
			logger.error("",ex);
			newsSubjectResultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return newsSubjectResultGson;
	}

	//hasDetail为false，只返回favorite和数量统计
	public NewsDetailResultGson getNewsDetail(Long newsID, Long userID, Boolean hasDetail)
	{
		NewsDetailResultGson newsDetailResultGson = new NewsDetailResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			NewsDetailDTO newsDetailDTO = null;
			if (hasDetail)
			{
				newsDetailDTO = newsDetailDao.selectNewsDetail(newsID);
				if (newsDetailDTO == null) {
					newsDetailDTO = new NewsDetailDTO();
					newsDetailDTO.setDetailContent("<b>你来晚了，改主题已经被删除了！</b>");
					newsDetailResultGson.setNewsDetailDTO(newsDetailDTO);
					return newsDetailResultGson;
				}

				UserDTO userDTO = userDao.selectByID(newsDetailDTO.getPublisher_id().toString());
				if (userDTO != null)
				{
					newsDetailDTO.setPublisher_avatar_url(userDTO.getAvatarUrl());
					newsDetailDTO.setPublisher_name(userDTO.getNickName());
					newsDetailDTO.setPublisher_id(newsDetailDTO.getPublisher_id());
				}
			}
			else {
				newsDetailDTO = new NewsDetailDTO();
			}

			Boolean bIsFav = newsServiceUtils.isFavoriteNews(userID, newsID);
			newsDetailDTO.setFavorite(bIsFav);
			newsDetailResultGson.setNewsDetailDTO(newsDetailDTO);

			CounterMsgGson counterMsgGson = new CounterMsgGson(newsID.toString());
			messageUtils.pushCounterMsg(counterMsgGson);
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

	public RetResultGson updateNewsSubject(String sessionID, Long beAdminID, NewsGson newsGson)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		if (!beAdminServiceUtils.isLoginIn(beAdminID, sessionID))
		{
			resultGson.setResult(RetCode.RET_ERROR_BEADMIN_SESSION_OUTDATE, RetMsg.RET_MSG_BEADMIN_SESSION_OUTDATE);
			return resultGson;
		}

		try {
			Integer nRow = newsDao.update(newsGson);
			if (nRow != 1) {
				resultGson.setResult(RetCode.RET_ERROR_INVALID_NEWSID, RetMsg.RET_MSG_INVALID_NEWSID);
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

	public RetResultGson updateNewsDetail(String sessionID, Long beadminID, NewsDetailGson newsDetailGson)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		if (!beAdminServiceUtils.isLoginIn(beadminID, sessionID))
		{
			resultGson.setResult(RetCode.RET_ERROR_BEADMIN_SESSION_OUTDATE, RetMsg.RET_MSG_BEADMIN_SESSION_OUTDATE);
			return resultGson;
		}
		try {
			Integer nRow = newsDetailDao.update(newsDetailGson);
			if (nRow != 1) {
				resultGson.setResult(RetCode.RET_ERROR_INVALID_NEWSID, RetMsg.RET_MSG_INVALID_NEWSID);
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
