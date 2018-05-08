package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.CommentsResultGson;
import com.school.Gson.RetBeAdminLoginGson;
import com.school.Utils.GsonUtil;
import com.school.service.BeAdminService;
import com.sun.jersey.api.core.InjectParam;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/beadmin")
public class BeAdminResource {
	Logger logger = Logger.getLogger(BeAdminResource.class.getName());

	@InjectParam
	private BeAdminService beAdminService;

	@POST
	@Path(("/login"))
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String loginRegister(@FormParam("username") String userName, @FormParam("password") String password) {
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
			RetBeAdminLoginGson resultGson = new RetBeAdminLoginGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		RetBeAdminLoginGson resultGson = beAdminService.adminLoginIn(userName, password);
		return GsonUtil.toJson(resultGson);
	}
}

