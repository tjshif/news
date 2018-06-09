package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.CommentsResultGson;
import com.school.Gson.RetResultGson;
import com.school.Utils.GsonUtil;
import com.school.service.FocusService;
import com.sun.jersey.api.core.InjectParam;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/focus")
public class FocusResource {
	private Logger logger = Logger.getLogger(getClass());

	@InjectParam
	private FocusService focusService;

	@POST
	@Path("/setfocus")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String setFocus(@FormParam("fromUserID") String fromUserID, @FormParam("toUserID") String toUserID)
	{
		if (TextUtils.isEmpty(fromUserID) || TextUtils.isEmpty(toUserID))
		{
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		else if (fromUserID.equalsIgnoreCase(toUserID))
		{
			RetResultGson resultGson = new RetResultGson(RetCode.RET_ERROR_SAME_USER, RetMsg.RET_MSG_SAME_USER);
			return GsonUtil.toJson(resultGson);
		}
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			resultGson = focusService.setFocus(fromUserID, toUserID);
		}
		catch (Exception ex)
		{
			logger.error("",ex);
			resultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return GsonUtil.toJson(resultGson);
	}
}
