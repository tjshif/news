package com.school.resouce;

import com.google.gson.JsonSyntaxException;
import com.school.AOP.LogAnnotation;
import com.school.Enum.NewsSubTypeEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.*;
import com.school.Utils.GsonUtil;
import com.school.service.NewsService;
import com.sun.jersey.api.core.InjectParam;
import org.apache.http.util.TextUtils;
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
			logger.error(String.format("invalid type: newsType:%d; newsSubTypeEnem:%d", newsType, subNewsType));
			NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_ERROR_INVALID_INPUT, RetMsg.RET_MSG_INVALID_INPUT);
			return GsonUtil.toJson(resultGson);
		}
		NewsSubjectResultGson retResult = newsService.getNewsSubjectList(newsTypeEnum, newsSubTypeNem, location, startFrom, count);
		return GsonUtil.toJson(retResult);
	}

	@GET
	@Path("/getnewslistsubjectsbypage")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getNewsSubjectListByPage(@QueryParam("newstype") Integer newsType,
											 @QueryParam("subnewstype") Integer subNewsType,
											 @QueryParam("location")Integer location,
											 @QueryParam("page")Integer page, //不包括startfrom
											 @DefaultValue("20")@QueryParam("pageSize")Integer pageSize)
	{
		NewsTypeEnum newsTypeEnum = null;
		NewsSubTypeEnum newsSubTypeNem = null;
		try {
			newsTypeEnum = NewsTypeEnum.valueToNews(newsType);
			newsSubTypeNem = (subNewsType != null ? NewsSubTypeEnum.valueToNewsSubType(subNewsType) : null);
		}
		catch (Exception ex)
		{
			logger.error(String.format("invalid type: newsType:%d; newsSubTypeEnem:%d", newsType, subNewsType));
			NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_ERROR_INVALID_INPUT, RetMsg.RET_MSG_INVALID_INPUT);
			return GsonUtil.toJson(resultGson);
		}
		NewsSubjectResultGson retResult = newsService.getNewsSubjectListByPage(newsTypeEnum, newsSubTypeNem, location, page, pageSize);
		return GsonUtil.toJson(retResult);
	}

	@GET
	@Path("/getnewsdetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getNewsDetail(@QueryParam("newsid") Long newsID, @QueryParam("userid")Long userID,
								@QueryParam("hasDetail")@DefaultValue("true") Boolean hasDetail)
	{
		NewsDetailResultGson resultGson = newsService.getNewsDetail(newsID, userID, hasDetail);
		return GsonUtil.toJson(resultGson);
	}

	@GET
	@Path("/getnewsbyid")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getNewsByID(@QueryParam("newsid") Long newsID, @QueryParam("userid")Long userID)
	{
		NewsSubjectResultGson resultGson = newsService.getNewsByID(newsID, userID);
		return GsonUtil.toJson(resultGson);
	}

	@GET
	@Path("/getnewsdetailbyurl")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getNewsDetailByUrl(@QueryParam("linkurl") String linkurl)
	{
		if (TextUtils.isEmpty(linkurl))
		{
			NewsDetailResultGson resultGson = new NewsDetailResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		NewsDetailResultGson resultGson = newsService.getNewsDetailByUrl(linkurl);
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
	public String getFavoriteNews(@QueryParam("userid") Long userID,
												 @DefaultValue("0")@QueryParam("page") Integer page,
												 @DefaultValue("20")@QueryParam("pageSize") Integer pageSize)
	{
		if (userID == null)
		{
			NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}

		NewsSubjectResultGson resultGson = newsService.getFavoriteNews(userID, page, pageSize);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/clearfav")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String clearFav(@FormParam("userid")Long userID)
	{
		if (userID == null)
		{
			NewsFavoriteResultGson resultGson = new NewsFavoriteResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		NewsFavoriteResultGson retResultGson = newsService.clearFav(userID);
		return GsonUtil.toJson(retResultGson);
	}

	@POST
	@Path("/updatenews")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String updateNews(@FormParam("newsid")Long newsID, @FormParam("isvalid") Boolean isValid)
	{
		if (newsID == null || isValid == null)
		{
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		RetResultGson resultGson = newsService.updateNewsValid(newsID, isValid);
		return GsonUtil.toJson(resultGson);
	}

	@GET
	@Path("/getnewscount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getNewsCount(@QueryParam("newstype") Integer newsType,
							   @QueryParam("subnewstype") Integer subNewsType,
							   @QueryParam("location")Integer location)
	{
		NewsTypeEnum newsTypeEnum = null;
		NewsSubTypeEnum newsSubTypeEnum = null;
		try {
			newsTypeEnum = NewsTypeEnum.valueToNews(newsType);
			newsSubTypeEnum = (subNewsType != null ? NewsSubTypeEnum.valueToNewsSubType(subNewsType) : null);
		}
		catch (Exception ex)
		{
			logger.error(String.format("invalid type: newsType:%d; newsSubTypeNem:%d", newsType, subNewsType));
			NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		NewsCountResultGson resultGson = newsService.getNewsCount(newsTypeEnum, newsSubTypeEnum, location);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/updatenewssubject")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String updateNewsSubject(@HeaderParam("SessionID") String sessionID, @FormParam("dto") String dto)
	{
		if (TextUtils.isEmpty(dto))
		{
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}

		NewsGson newsGson = null;
		try {
			newsGson = GsonUtil.fromJson(dto, NewsGson.class);
		}
		catch (JsonSyntaxException ex)
		{
			RetResultGson resultGson = new RetResultGson(RetCode.RET_ERROR_INVLAID_GSON_STRING, RetMsg.RET_MSG_INVLAID_GSON_STRING);
			return GsonUtil.toJson(resultGson);
		}

		RetResultGson resultGson = newsService.updateNewsSubject(sessionID, newsGson);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/updatenewsdetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String updateNewsDetail(@HeaderParam("SessionID") String sessionID, @FormParam("dto") String dto)
	{
		if (TextUtils.isEmpty(dto))
		{
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}

		NewsDetailGson newsDetailGson = null;
		try {
			newsDetailGson = GsonUtil.fromJson(dto, NewsDetailGson.class);
		}
		catch (JsonSyntaxException ex)
		{
			RetResultGson resultGson = new RetResultGson(RetCode.RET_ERROR_INVLAID_GSON_STRING, RetMsg.RET_MSG_INVLAID_GSON_STRING);
			return GsonUtil.toJson(resultGson);
		}

		RetResultGson resultGson = newsService.updateNewsDetail(sessionID, newsDetailGson);
		return GsonUtil.toJson(resultGson);
	}
}