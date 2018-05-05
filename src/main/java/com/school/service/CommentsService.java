package com.school.service;

import com.school.AOP.CacheMethodLogo;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IUnReadMesssageDao;
import com.school.Entity.*;
import com.school.Gson.*;
import com.school.Utils.TimeUtils;
import com.school.service.common.NewsServiceUtils;
import com.school.service.common.UserCommonServiceUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.school.DAO.ICommentDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.sql.Timestamp;
import java.util.List;

@Service
public class CommentsService {
	@Resource
	UserCommonServiceUtil userCommonServiceUtil;

	private Logger logger = Logger.getLogger(CommentsService.class.getName());

	@Resource
	private ICommentDao commentDao;

	@Resource
	private IUnReadMesssageDao unReadMesssageDao;

	@Resource
	private NewsServiceUtils newsServiceUtils;

	@Transactional
	public RetFLCommentResultGson addFLComment(Long newsID, Long userID, String comment)
	{
		RetFLCommentResultGson retResultGson = new RetFLCommentResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		UserDTO userDTO = userCommonServiceUtil.getUserDTO(userID);
		if (userDTO == null)
		{
			retResultGson.setResult(RetCode.RET_ERROR_INVALID_USERID, RetMsg.RET_MSG_INVALID_USERID);
			return retResultGson;
		}

		NewsDTO newsDTO = newsServiceUtils.getNews(newsID);
		if (newsDTO == null)
		{
			retResultGson.setResult(RetCode.RET_ERROR_INVALID_NEWSID, RetMsg.RET_MSG_INVALID_NEWSID);
			return retResultGson;
		}

		FirstLevelCommentDTO firstLevelCommentDTO = new FirstLevelCommentDTO(newsID, newsDTO.getSubject(), userID, userDTO.getNickName(),
				userDTO.getAvatarUrl(), comment);
		try {
			commentDao.insertFirstLevelComment(firstLevelCommentDTO);
			firstLevelCommentDTO.setCreateAt(new Timestamp(System.currentTimeMillis()));
			retResultGson.setFirstLevelCommentDTO(firstLevelCommentDTO);
		}
		catch (Exception ex)
		{
			logger.error(ex);
			retResultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return retResultGson;
	}

	@Transactional
	public RetSecCommentResultGson addSecComment(Long flID, Long fromUserID, Long toUserID, String replyComment)
	{
		RetSecCommentResultGson retResultGson = new RetSecCommentResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		UserDTO fromUserDTO = userCommonServiceUtil.getUserDTO(fromUserID);
		UserDTO toUserDTO = userCommonServiceUtil.getUserDTO(toUserID);
		if (fromUserDTO == null || toUserDTO == null)
		{
			logger.error("fromUserID: " + fromUserID + " toUserID: " + toUserID);
			retResultGson.setResult(RetCode.RET_ERROR_INVALID_USERID, RetMsg.RET_MSG_INVALID_USERID);
			return retResultGson;
		}

		SecondLevelCommentDTO secondLevelCommentDTO = new SecondLevelCommentDTO(flID, fromUserID, fromUserDTO.getNickName(), fromUserDTO.getAvatarUrl(),
				toUserID, toUserDTO.getNickName(), toUserDTO.getAvatarUrl(), replyComment);
		commentDao.insertSecondLevelComment(secondLevelCommentDTO);
		secondLevelCommentDTO.setCreateAt(new Timestamp(System.currentTimeMillis()));
		retResultGson.setSecondLevelCommentDTO(secondLevelCommentDTO);
		commentDao.increaseCommentCount(flID);

		if (fromUserID.intValue() != toUserID.intValue())
		{
			UnReadMesssageDTO unReadMesssageDTO = new UnReadMesssageDTO(toUserID);
			unReadMesssageDao.insert(unReadMesssageDTO);
		}

		return retResultGson;
	}

	@Transactional
	public RetResultGson removeComments(Long ID)
	{
		RetResultGson retResultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			commentDao.deleteComment(ID);
		}
		catch (Exception ex)
		{
			logger.error(ex);
			retResultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return retResultGson;
	}

	@Transactional
	public RetResultGson removeReplyComments(Long ID)
	{
		RetResultGson retResultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);

		SecondLevelCommentDTO secondLevelCommentDTO = commentDao.selectSLCommentByID(ID);
		if (secondLevelCommentDTO == null)
			return retResultGson;

		int row = commentDao.deleteReplyComment(ID);
		if (row == 1)
			commentDao.decreaseCommentCount(secondLevelCommentDTO.getFlID());
		return retResultGson;
	}

	public CommentsResultGson getComments(Long newsID, Integer page, Integer pageSize)
	{
		CommentsResultGson commentsResultGson = new CommentsResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		Integer offset = page * pageSize;

		try {
			List<FirstLevelCommentDTO> firstLevelComments = commentDao.selectComments(newsID, offset, pageSize);
			commentsResultGson.setFirstLevelCommentDTOS(firstLevelComments);

			if (firstLevelComments.size() > 0)
			{
				List<FirstLevelCommentDTO> flComments = selectFLComments(newsID);
				int nCount = 0;
				for (FirstLevelCommentDTO firstLevelCommentDTO : flComments)
				{
					nCount += firstLevelCommentDTO.getCount();
				}
				commentsResultGson.setTotoalCount(nCount);

				if (flComments.get(flComments.size() - 1).getId().equalsIgnoreCase(firstLevelComments.get(firstLevelComments.size() - 1).getId()))
					commentsResultGson.setHasMore(false);
			}
		}
		catch (Exception ex)
		{
			logger.error(ex);
			commentsResultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return commentsResultGson;
	}

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS)
	public List<FirstLevelCommentDTO> selectFLComments(Long newsID)
	{
		if (newsID == null)
			return null;
		return commentDao.selectFLComments(newsID);
	}

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS)
	public MyCommentsResultGson selectMyComments(Long userID, Integer page, Integer pageSize)
	{
		MyCommentsResultGson resultGson = new MyCommentsResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		Integer offset = page * pageSize;

		try {
			List<FirstLevelCommentDTO> firstLevelCommentDTOS = commentDao.selectMyComments(userID, offset, pageSize);
			resultGson.setFirstLevelCommentDTOS(firstLevelCommentDTOS);
		}
		catch (Exception ex)
		{
			logger.error(ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return resultGson;
	}
}
