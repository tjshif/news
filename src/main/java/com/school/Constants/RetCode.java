package com.school.Constants;

public class RetCode {
	public static final int RET_CODE_SYSTEMERROR= -1; //System Error

	public static final int RET_CODE_OK = 0; //OK
	public static final int RET_CODE_REQUIREEMPTY = 1; //必填项为空

	public static final int RET_ERROR_FIND_ROW = 0x3001;//没有找到记录

	public static final int RET_ERROR_CODE_SENDSMSMAXCOUNT = 0X1001; //Send SMS error
	public static final int RET_CODE_GETSMSFIRST = 0X1002; //Send SMS error
	public static final int RET_CODE_SMSERROR = 0X1003; //验证码错误
	public static final int RET_ERROR_CODE_PHONENUMBER = 0X1004; //PHONENUMBER error

}
