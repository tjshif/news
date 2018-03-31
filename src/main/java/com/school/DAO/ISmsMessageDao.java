package com.school.DAO;

import com.school.Entity.SmsMessageDTO;

public interface ISmsMessageDao {
	SmsMessageDTO selectSmsSendToday(String phoneNumber);
}
