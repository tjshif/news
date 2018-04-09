package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Gson.CityResultGson;
import com.school.Utils.GsonUtil;
import com.school.service.ConfigureService;
import com.sun.jersey.api.core.InjectParam;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/config")
public class ConfigureResource {
	@InjectParam
	private ConfigureService configureService;

	@GET
	@Path("/supportedcities")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getSupportedCities()
	{
		CityResultGson cityResultGson = configureService.getSupportedCities();
		return GsonUtil.toJson(cityResultGson);
	}
}
