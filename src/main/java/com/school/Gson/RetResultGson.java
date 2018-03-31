package com.school.Gson;

public class RetResultGson {
	private int    errcode;
	private String errmsg;

	public RetResultGson(int retCode, String message)
	{
		setResult(retCode, message);
	}

	public void setResult(int retCode, String message)
	{
		this.errcode = retCode;
		this.errmsg = message;
	}
	public int getRetCode() {
		return errcode;
	}

	public void setRetCode(int retCode) {
		this.errcode = retCode;
	}

	public String getMessage() {
		return errmsg;
	}

	public void setMessage(String message) {
		this.errmsg = message;
	}
}
