package com.school.service;

import com.school.Constants.MsgType;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.IFocusDao;
import com.school.Entity.FocusDTO;
import com.school.Entity.UserDTO;
import com.school.Gson.RetResultGson;
import com.school.Msg.LoadToRedisMsg;
import com.school.PushService.PushLoadToRedisMsgService;
import com.school.service.common.UserCommonServiceUtil;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FocusService {
	private Logger logger = Logger.getLogger(getClass());

	@Resource
	UserCommonServiceUtil userCommonServiceUtil;

	@Resource
	IFocusDao focusDao;

	@Resource
	PushLoadToRedisMsgService pushLoadToRedisMsgService;

	@Transactional
	public RetResultGson setFocus(String fromUserID, String toUserID)
	{
		RetResultGson resultGson = new RetResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);

		UserDTO fromUserDto = userCommonServiceUtil.selectUser(fromUserID);
		UserDTO toUserDto = userCommonServiceUtil.selectUser(toUserID);

		if (fromUserDto == null || toUserDto == null)
		{
			resultGson.setResult(RetCode.RET_ERROR_INVALID_USERID, RetMsg.RET_MSG_INVALID_NEWSID);
			logger.error("fromUserID: " + fromUserID + "; toUserID:" + toUserID);
			return resultGson;
		}

		List<String> ids = setFocus(fromUserDto, toUserDto);
		for (String id : ids)
		{
			pushLoadMsg(id);
		}
		return resultGson;
	}

	private List<String> setFocus(UserDTO fromUserDto, UserDTO toUserDto)
	{
		if (fromUserDto == null || toUserDto == null)
			return null;

		List<String> updateIDs = new ArrayList<>();
		FocusDTO fromToUserFocusDto = new FocusDTO(fromUserDto.getId(), toUserDto.getId(), false);
		FocusDTO toUserToFromFocusDto = focusDao.selectFocus(toUserDto.getId(), fromUserDto.getId());

		if (toUserToFromFocusDto != null)
		{
			toUserToFromFocusDto.setBothStatus(true);
			focusDao.update(toUserToFromFocusDto);
			updateIDs.add(toUserToFromFocusDto.getId());

			fromToUserFocusDto.setBothStatus(true);
		}

		focusDao.insert(fromToUserFocusDto);
		if (TextUtils.isEmpty(fromToUserFocusDto.getId()))
		{
			fromToUserFocusDto = focusDao.selectFocus(fromUserDto.getId(), toUserDto.getId());
		}
		updateIDs.add(fromToUserFocusDto.getId());
		return updateIDs;
	}

	private void pushLoadMsg(String id)
	{
		if (TextUtils.isEmpty(id))
			return;

		LoadToRedisMsg msg = new LoadToRedisMsg(MsgType.MSG_FOCUSTABLE, id);
		pushLoadToRedisMsgService.push(msg);
	}
}
