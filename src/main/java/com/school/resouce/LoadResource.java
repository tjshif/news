package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Gson.RetResultGson;
import com.school.Redis.LoadDataToRedis;
import com.school.Utils.GsonUtil;
import com.sun.jersey.api.core.InjectParam;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/load")
public class LoadResource {
	@InjectParam
	private LoadDataToRedis loadDataToRedis;

	@POST
	@Path("/cache")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String loadToRedisForTag(@FormParam("tag") String tag)
	{
		RetResultGson resultGson = loadDataToRedis.loadDataToRedis(tag);
		return GsonUtil.toJson(resultGson);
	}
}
