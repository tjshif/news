package com.school.resouce;

import com.google.gson.Gson;
import com.school.AOP.LogAnnotation;
import com.school.Constants.NewsTypeConst;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.NewsSubjectResultGson;
import com.school.Utils.GsonUtil;
import com.school.service.NewsService;
import com.sun.jersey.api.core.InjectParam;
import org.apache.ibatis.annotations.Param;
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
	@Path("/getnewssubject")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getNewsSubjectList(@QueryParam("newstype") Integer newsType,
									 @QueryParam("subnewstype") Integer subNewsType,
									 @QueryParam("location")Integer location,
									 @QueryParam("startfrom")Integer startFrom,
									 @QueryParam("count")Integer count)
	{
		if (!NewsTypeConst.isValidType(newsType) || count.intValue() < 1)
		{
			logger.error(String.format("invalid newsType:%d;Invalid count:%d", newsType,count));
			NewsSubjectResultGson resultGson = new NewsSubjectResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		NewsSubjectResultGson retResult = newsService.getNewsSubjectList(newsType, subNewsType, location, startFrom, count);
		return GsonUtil.toJson(retResult);
	}
}
