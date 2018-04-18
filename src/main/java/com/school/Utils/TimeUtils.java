package com.school.Utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	public static final long ONE_MINUTE_MILLIONS = 60 * 1000;
	public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
	public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;
	public static final int  ONE_MINUTE_SECONDS = 60;

	public static String getDay(Date date)
	{
		if (date == null)
			return "";
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
		return sfd.format(date);
	}
}