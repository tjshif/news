package com.school.PushService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

@Service
public class PushCounterMsgService extends PushService{
	@Autowired
	@Qualifier("queueCounterMsgDestination")
	private Destination destination;

	@Override
	public Destination getDestination() {
		return destination;
	}
}
