package com.school.Listener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component("consumeLoadToRedisMsgToDBListener")
public class ConsumeLoadToRedisMsgToDBListener implements MessageListener {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void onMessage(Message message) {
		TextMessage txtMsg = (TextMessage)message;
		try {
			logger.info(txtMsg.getText());


		}
		catch (Exception ex)
		{
			logger.error("",ex);
		}
	}
}
