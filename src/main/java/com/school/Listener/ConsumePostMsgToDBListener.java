package com.school.Listener;

import com.school.Entity.MsgAggregate;
import com.school.Redis.RedisHandler;
import com.school.Utils.ApplicationContextUtils;
import com.school.Utils.GsonUtil;
import com.school.service.common.NewsServiceUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component("consumePostMsgToDBListener")
public class ConsumePostMsgToDBListener implements MessageListener{
	@Resource
	NewsServiceUtils newsServiceUtils;

	@Resource
	RedisHandler redisHandler;

	private Logger logger = Logger.getLogger(ConsumePostMsgToDBListener.class.getName());
	@Override
	public void onMessage(Message message) {
		TextMessage txtMsg = (TextMessage)message;
		MsgAggregate msgAggregate = null;

		try {
			logger.info(txtMsg.getText());
			msgAggregate = GsonUtil.fromJson(txtMsg.getText(), MsgAggregate.class);
			newsServiceUtils.saveMsgToDB(msgAggregate);
		}
		catch (Exception ex)
		{
			logger.error(ex);
			if (msgAggregate != null && msgAggregate.getNewsDTO() != null)
				redisHandler.removeNewsFromRedis(msgAggregate.getNewsDTO());
		}
	}
}
