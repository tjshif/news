package com.school.Utils;

import com.school.Entity.MsgAggregate;
import com.school.Gson.CounterMsgGson;
import com.school.PushService.PushCounterMsgService;
import com.school.PushService.PushPostMsgToDBService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageUtils {
	@Resource
	private PushCounterMsgService pushCounterMsgService;

	@Resource
	private PushPostMsgToDBService pushPostMsgToDBService;

	public void pushCounterMsg(CounterMsgGson counterMsgGson)
	{
		if (counterMsgGson == null)
			return;

		pushCounterMsgService.push(counterMsgGson);
	}

	public void pushPostMsg(MsgAggregate msgAggregate)
	{
		if (msgAggregate == null)
			return;
		pushPostMsgToDBService.push(msgAggregate);
	}
}
