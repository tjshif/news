package com.school.Constants;

public class NewsTypeConst {
	public static final Integer FRIENDS  =  1;
	private static final Integer  MAXTYPE = FRIENDS;

	public static Boolean isValidType(Integer type)
	{
		if (type < FRIENDS || type > MAXTYPE)
			return false;
		else
			return true;
	}
}
