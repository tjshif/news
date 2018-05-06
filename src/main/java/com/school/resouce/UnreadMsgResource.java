package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.RetResultGson;
import com.school.Gson.UnreadMsgResultGson;
import com.school.Utils.GsonUtil;
import com.school.service.UnreadMsgService;
import com.sun.jersey.api.core.InjectParam;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

@Component
@Path("/msg")
public class UnreadMsgResource {
	Logger logger = Logger.getLogger(UnreadMsgResource.class.getName());

	@InjectParam
	private UnreadMsgService unreadMsgService;

	@GET
	@Path("/getunreadmsginfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getUnreadMsgInfo(@QueryParam("userID") Long userID)
	{
		if (userID == null) {
			UnreadMsgResultGson resultGson = new UnreadMsgResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		UnreadMsgResultGson resultGson = unreadMsgService.getUnreadMsgs(userID);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/clearunreadmsg")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String clearUnRead(@FormParam("userID") Long userID)
	{
		if (userID == null) {
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		RetResultGson resultGson = unreadMsgService.clearUnRead(userID);
		return GsonUtil.toJson(resultGson);
	}

}
