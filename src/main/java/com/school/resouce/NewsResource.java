package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Enum.NewsSubTypeEnum;
import com.school.Enum.NewsTypeEnum;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.NewsDetailResultGson;
import com.school.Gson.NewsSubjectResultGson;
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
	public String getNewsDetail(@QueryParam("newsid") Long newsID)
	{
		NewsDetailResultGson resultGson = newsService.getNewsDetail(newsID);
		return GsonUtil.toJson(resultGson);
	}
}
