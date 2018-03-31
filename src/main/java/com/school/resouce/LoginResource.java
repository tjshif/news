package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Gson.LoginRegisterGson;
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
		}
		LoginRegisterGson retResult = mLoginService.loginRegister(phoneNumber, smsCode);

		return GsonUtil.toJson(retResult);
	}
}
