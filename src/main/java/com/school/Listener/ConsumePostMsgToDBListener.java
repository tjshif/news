package com.school.Listener;

import com.school.DAO.IPostmsgDao;
import com.school.Entity.PostmsgDTO;
import com.school.Utils.ApplicationContextUtils;
import com.school.Utils.GsonUtil;
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
			PostmsgDTO postmsgDTO = GsonUtil.fromJson(txtMsg.getText(), PostmsgDTO.class);

			IPostmsgDao postmsgDao = ApplicationContextUtils.getBean(IPostmsgDao.class);
			if (postmsgDao != null)
				postmsgDao.insert(postmsgDTO);
			else
				logger.error("fail to insert postmsgdto to db: " + txtMsg.getText());
		}
		catch (Exception ex)
		{
			logger.error(ex);
		}
	}
}
