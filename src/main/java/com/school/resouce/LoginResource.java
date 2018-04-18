package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.LoginRegisterGson;
import com.school.Gson.RetResultGson;
import com.school.Gson.VersionResultGson;
import com.school.Utils.GsonUtil;
import com.school.service.LoginService;
import com.sun.jersey.api.core.InjectParam;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/login")
public class LoginResource {
	Logger logger = Logger.getLogger(LoginResource.class.getName());

	@InjectParam
	private LoginService  mLoginService;

	//通过短信登录或注册接口
	@POST
	@Path(("/loginregister"))
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String loginRegister(@FormParam("phoneNo") String phoneNumber, @FormParam("smsCode") String smsCode)
	{
		logger.info(String.format("phoneNo: %s; smsCode:%s", phoneNumber, smsCode));
		phoneNumber = phoneNumber.trim();
		smsCode = smsCode.trim();
		if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(smsCode))
		{
			logger.error(String.format("invalid loginregister:phoneNo: %s; smsCode:%s", phoneNumber, smsCode));
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		LoginRegisterGson retResult = mLoginService.loginRegister(phoneNumber, smsCode);

		return GsonUtil.toJson(retResult);
	}

	//用户反馈
	@POST
	@Path("/sendfeedback")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String sendFeedback(@FormParam("contactInfo") String contactInfo, @FormParam("feedback") String feedback)
	{
		if (TextUtils.isEmpty(feedback))
		{
			logger.error("feedback can't be empty!");
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		RetResultGson resultGson = mLoginService.insertFeedback(contactInfo, feedback);
		return GsonUtil.toJson(resultGson);
	}

	@GET
	@Path("/latestverion")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String latestVersion(@QueryParam("versionid") Long versionid)
	{
		if (versionid == null)
		{
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		VersionResultGson versionResultGson = mLoginService.getVersionInfo(versionid);
		return GsonUtil.toJson(versionResultGson);
	}
}
