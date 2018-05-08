package com.school.Constants;

public class RetCode {
	public static final int RET_CODE_SYSTEMERROR= -1; //System Error

	public static final int RET_CODE_OK = 0; //OK
	public static final int RET_CODE_REQUIREEMPTY = 1; //必填项为空

	public static final int RET_ERROR_CODE_SENDSMSMAXCOUNT = 0X1001; //Send SMS error
	public static final int RET_CODE_GETSMSFIRST = 0X1002; //Send SMS error
	public static final int RET_CODE_SMSERROR = 0X1003; //验证码错误
	public static final int RET_ERROR_CODE_PHONENUMBER = 0X1004; //PHONENUMBER error


	public static final int RET_ERROR_FIND_ROW = 0x3001;//没有找到记录
	public static final int RET_ERROR_INVALID_USERID = 0x3002;//无效用户ID
	public static final int RET_ERROR_INVALID_REPLYCOMMENT = 0x3003; //"评论已经删除，无法回复了";
	public static final int RET_ERROR_INVALID_NEWSID = 0x3004;//invalid news ID;

	public static final int RET_ERROR_USRE_INVALID_NICKNAME = 0x4001;//nickname已经被使用了
	public static final int RET_ERROR_USER_INVALID_FORMAT = 0x4002;//仅支持中文、字母、数字

	public static final int RET_ERROR_USER_INVALID_INFO = 0x5001;//无效用户名或密码


}
