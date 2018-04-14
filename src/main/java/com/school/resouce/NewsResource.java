package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Enum.NewsSubTypeEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.NewsDetailResultGson;
import com.school.Gson.NewsFavoriteResultGson;
import com.school.Gson.NewsSubjectResultGson;
import com.school.Gson.RetResultGson;
import com.school.Utils.GsonUtil;
import com.school.service.NewsService;
import com.sun.jersey.api.core.InjectParam;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/news")
public class NewsResource {
	Logger logger = Logger.getLogger(NewsResource.class.getName());

	@InjectParam
	private NewsService newsService;

	@GET
	@Path("/getnewslistsubjects")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getNewsSubjectList(@QueryParam("newstype") Integer newsType,
									 @QueryParam("subnewstype") Integer subNewsType,
									 @QueryParam("location")Integer location,
									 @QueryParam("startfrom")Long startFrom, //不包括startfrom
									 @DefaultValue("20")@QueryParam("count")Integer count)
	{
		NewsTypeEnum newsTypeEnum = null;
		NewsSubTypeEnum newsSubTypeNem = null;
		try {
			newsTypeEnum = NewsTypeEnum.valueToNews(newsType);
			newsSubTypeNem = (subNewsType != null ? NewsSubTypeEnum.valueToNewsSubType(subNewsType) : null);
		}
		catch (Exception ex)
		{
			logger.error(String.format("invalid type: newsType:%d; newsSubTypeNem:%d", newsType, subNewsType));
			NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		NewsSubjectResultGson retResult = newsService.getNewsSubjectList(newsTypeEnum, newsSubTypeNem, location, startFrom, count);
		return GsonUtil.toJson(retResult);
	}

	@GET
	@Path("/getnewsdetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getNewsDetail(@QueryParam("newsid") Long newsID, @QueryParam("userid")Long userID)
	{
		NewsDetailResultGson resultGson = newsService.getNewsDetail(newsID, userID);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/addorremovefavoritenews")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String addOrDeleteFavoriteNews(@FormParam("addordelete") Boolean bAdd,
										  @FormParam("userid")Long userID,
										  @FormParam("newsid") Long newsID)
	{
		if (userID == null || newsID == null)
		{
			NewsFavoriteResultGson resultGson = new NewsFavoriteResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		NewsFavoriteResultGson retResultGson = newsService.addOrDeleteFavoriteNews(bAdd, userID, newsID);
		return GsonUtil.toJson(retResultGson);
	}

	@GET
	@Path("/getisfavorite")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getIsFavorite(@QueryParam("newsid") Long newsID, @QueryParam("userid")Long userID)
	{
		if (newsID == null)
		{
			NewsFavoriteResultGson resultGson = new NewsFavoriteResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		NewsFavoriteResultGson resultGson = newsService.getIsFavorite(newsID, userID);
		return GsonUtil.toJson(resultGson);
	}

	@GET
	@Path("/getfavoritenews")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getFavoriteNews(@QueryParam("newsid") Long userID,
												 @DefaultValue("0")@QueryParam("offset") Long offset,
												 @DefaultValue("20")@QueryParam("pageSize") Integer pageSize)
	{
		if (userID == null)
		{
			NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}

		NewsSubjectResultGson resultGson = newsService.getFavoriteNews(userID, offset, pageSize);
		return GsonUtil.toJson(resultGson);
	}
}