package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.CommentsResultGson;
import com.school.Gson.RetIDResultGson;
import com.school.Utils.GsonUtil;
import com.school.service.CommentsService;
import com.sun.jersey.api.core.InjectParam;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/comment")
public class CommentsResouce {
	Logger logger = Logger.getLogger(CommentsResouce.class.getName());

	@InjectParam
	private CommentsService commentsService;

	@GET
	@Path("/getcomments")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getComments(@QueryParam("newsID") Long newsID,
							  @QueryParam("page") Integer page,
							  @QueryParam("pageSize")@DefaultValue("5") Integer pageSize)
	{
		if (newsID == null || page == null)
		{
			CommentsResultGson resultGson = new CommentsResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}

		CommentsResultGson resultGson = commentsService.getComments(newsID, page, pageSize);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/addcomment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String addComment(@FormParam("newsID") Long newsID,
							  @FormParam("userID") Long userID,
							  @FormParam("comment") String comment)
	{
		if (newsID == null || userID == null || TextUtils.isEmpty(comment))
		{
			RetIDResultGson resultGson = new RetIDResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		RetIDResultGson resultGson = commentsService.addFLComment(newsID, userID, comment);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/replycomment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String replyComment(@FormParam("flID") Long flID,
							   @FormParam("fromUserID") Long fromUserID,
							   @FormParam("toUserID") Long toUserID,
							   @FormParam("replyComment") String replyComment)
	{
		if (flID == null || fromUserID == null || toUserID == null || TextUtils.isEmpty(replyComment))
		{
			RetIDResultGson resultGson = new RetIDResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		RetIDResultGson retResultGson = new RetIDResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);

		try {
			retResultGson = commentsService.addSecComment(flID, fromUserID, toUserID, replyComment);
		}
		catch (DataIntegrityViolationException ex)
		{
			logger.error(ex.getMessage());
			retResultGson.setResult(RetCode.RET_ERROR_INVALID_REPLYCOMMENT, RetMsg.RET_MSG_INVALID_REPLYCOMMENT);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			retResultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}

		return GsonUtil.toJson(retResultGson);
	}
}
