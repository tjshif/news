package com.school.Listener;

import com.school.Constants.MsgType;
import com.school.DAO.IFocusDao;
import com.school.Entity.BaseDTO;
import com.school.Msg.LoadToRedisMsg;
import com.school.Redis.RedisHandler;
import com.school.Utils.GsonUtil;
import com.school.Utils.TimeUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component("consumeLoadToRedisMsgToDBListener")
public class ConsumeLoadToRedisMsgToDBListener implements MessageListener {
	private Logger logger = Logger.getLogger(getClass());

	@Resource
	IFocusDao focusDao;

	@Resource
	RedisHandler redisHandler;

	@Override
	public void onMessage(Message message) {
		TextMessage txtMsg = (TextMessage)message;
		try {
			logger.info(txtMsg.getText());

			LoadToRedisMsg loadToRedisMsg = GsonUtil.fromJson(txtMsg.getText(), LoadToRedisMsg.class);
			if (TextUtils.isEmpty(loadToRedisMsg.getId()))
			{
				logger.error("invalid id");
				return;
			}

			BaseDTO baseDTO = null;
			if (loadToRedisMsg.getType() == MsgType.MSG_FOCUSTABLE)
			{
				baseDTO = focusDao.selectByID(loadToRedisMsg.getId());
			}
			else
			{
				logger.error("invalid type: " + loadToRedisMsg.getType());
				return;
			}

			if (baseDTO == null)
			{
				logger.error(String.format("can't find item: type: %d; id: %s", loadToRedisMsg.getType(), loadToRedisMsg.getId()));
				return;
			}
			redisHandler.loadItemToRedis(baseDTO, TimeUtils.ONE_DAY_SECONDS);
		}
		catch (Exception ex)
		{
			logger.error("",ex);
		}
	}
}
