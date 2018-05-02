package com.school.resouce;

import com.school.AOP.LogAnnotation;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.Gson.RetResultGson;
import com.school.Gson.UserInfoGson;
import com.school.Gson.UserInfoResultGson;
import com.school.Utils.GsonUtil;
import com.school.service.UserService;
import com.sun.jersey.api.core.InjectParam;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/user")
public class UserResource {
	Logger logger = Logger.getLogger(UserResource.class.getName());

	@InjectParam
	private UserService userService;

	@POST
	@Path("/{userID}/updatenickname")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String updateNickName(@PathParam("userID") Long userID, @FormParam("nickName") String nickName)
	{
		if (TextUtils.isEmpty(nickName) || userID == null)
		{
			logger.error("updateNick params is null or empty");
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}

		RetResultGson resultGson = userService.updateUserNickName(userID, nickName);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/{userID}/updatesex")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String updateSex(@PathParam("userID") Long userID, @FormParam("sex") Boolean sex)
	{
		if (userID == null || sex == null)
		{
			logger.error("updateSex params is null or empty");
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		RetResultGson resultGson = userService.updateSex(userID, sex);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/{userID}/updatecollege")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String updateCollege(@PathParam("userID") Long userID, @FormParam("college") String college)
	{
		if (userID == null || TextUtils.isEmpty(college))
		{
			logger.error("updateCollege params is null or empty");
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		RetResultGson resultGson = userService.updateCollege(userID, college);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/{userID}/updateavatarUrl")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String updateAvatarUrl(@PathParam("userID") Long userID, @FormParam("avatarUrl") String avatarUrl)
	{
		if (userID == null || TextUtils.isEmpty(avatarUrl))
		{
			logger.error("updateAvatarUrl params is null or empty");
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		RetResultGson resultGson = userService.updateAvatarUrl(userID, avatarUrl);
		return GsonUtil.toJson(resultGson);
	}

	@POST
	@Path("/{userID}/updateuserinfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String updateUserInfo(@PathParam("userID") Long userID, @FormParam("dto") String dto)
	{
		if (userID == null || TextUtils.isEmpty(dto))
		{
			logger.error("updateUserInfo params is null or empty");
			RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		UserInfoGson userInfoGson = GsonUtil.fromJson(dto, UserInfoGson.class);
		RetResultGson resultGson = userService.updateUserInfo(userID, userInfoGson);
		return GsonUtil.toJson(resultGson);
	}

	@GET
	@Path("/{userID}/getuserinfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@LogAnnotation
	public String getUserInfo(@PathParam("userID") Long userID)
	{
		if (userID == null)
		{
			logger.error("getUserInfo params is null or empty");
			UserInfoResultGson resultGson = new UserInfoResultGson(RetCode.RET_CODE_REQUIREEMPTY, RetMsg.RET_MSG_REQUIREEMPTY);
			return GsonUtil.toJson(resultGson);
		}
		UserInfoResultGson resultGson = userService.getUserInfo(userID);
		return GsonUtil.toJson(resultGson);
	}

}
