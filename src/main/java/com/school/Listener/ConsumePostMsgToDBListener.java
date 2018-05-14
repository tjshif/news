package com.school.Listener;

import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ConsumePostMsgToDBListener implements MessageListener{

	private Logger logger = Logger.getLogger(ConsumePostMsgToDBListener.class.getName());
	@Override
	public void onMessage(Message message) {
		TextMessage txtMsg = (TextMessage)message;
		try {
			logger.info(txtMsg.getText());
		}
		catch (JMSException ex)
		{
			logger.error(ex);
		}
	}
}
