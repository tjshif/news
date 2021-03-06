package com.school.PushService;

import com.school.Utils.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Destination;

@Service
public class PushPostMsgToDBService extends PushService {
	@Autowired
	@Qualifier("queuepostmsgDestination")
	private Destination destination;

	@Override
	public Destination getDestination() {
		return destination;
	}
}
