package com.school.Listener;

import com.school.Gson.CounterMsgGson;
import com.school.Utils.GsonUtil;
import com.school.service.common.NewsServiceUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component("consumeCounterMsgToDBListener")
public class ConsumeCounterMsgToDBListener implements MessageListener {
	private Logger logger = Logger.getLogger(getClass());
	@Resource
	private NewsServiceUtils newsServiceUtils;

	@Override
	public void onMessage(Message message) {
		TextMessage txtMsg = (TextMessage)message;
		try {
			logger.info(txtMsg.getText());
			CounterMsgGson counterMsgGson = GsonUtil.fromJson(txtMsg.getText(), CounterMsgGson.class);
			newsServiceUtils.increaseVisitCount(counterMsgGson);
		}
		catch (Exception ex)
		{
			logger.error("",ex);
		}
	}
}
