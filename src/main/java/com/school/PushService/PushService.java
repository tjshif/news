package com.school.PushService;

import com.school.Utils.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class PushService {
	private final ExecutorService pushExecutor = Executors.newFixedThreadPool(10);

	@Autowired
	private JmsTemplate jmsTemplate;

	public void push(Object info) {
		pushExecutor.execute(new Runnable() {
			@Override
			public void run() {
				jmsTemplate.send(getDestination(), new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(GsonUtil.toJson(info));
					}
				});
			}
		});
	}

	public abstract Destination getDestination();
}
