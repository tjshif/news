package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Entity.Channel;
import com.school.Enum.NewsEnum;
import com.school.Gson.ChannelResultGson;
import com.school.Gson.CityResultGson;
import com.school.Utils.AppProperites;
import com.school.Utils.GsonUtil;
import com.school.service.ConfigureService;
import com.sun.jersey.api.core.InjectParam;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/config")
public class ConfigureResource {
	@InjectParam
	private ConfigureService configureService;

	@InjectParam
	private AppProperites appProperites;

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

	@GET
	@Path("/supportedchannels")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getSupportedChannels()
	{
		ChannelResultGson resultGson = new ChannelResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);

		NewsEnum[] enums = NewsEnum.values();
		List<Channel> channels = new ArrayList<>();
		for (int ii = 0; ii < enums.length; ++ii)
		{
			Channel channel = new Channel(enums[ii].getNewsType(), enums[ii].getChannelName());
			channels.add(channel);
		}
		resultGson.setChannels(channels);
		resultGson.setChannelVersion(appProperites.getChannel_version());
		return GsonUtil.toJson(resultGson);
	}
}
