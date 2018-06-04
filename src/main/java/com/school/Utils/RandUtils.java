package com.school.Utils;

public class RandUtils {
	public static Boolean isRandPick(Integer percent)
	{
		if (percent == null || percent == 0)
			return false;

		int rand = (int)(Math.random() * 100);
		if (rand < percent)
			return true;
		return false;
	}

	public static Long random(Long topValue)
	{
		if (topValue == null || topValue < 1)
			return null;

		return (long)(Math.random() * topValue);
	}
}
