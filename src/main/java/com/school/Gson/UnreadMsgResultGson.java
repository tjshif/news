package com.school.Gson;

import com.school.Entity.UnReadMesssageDTO;

public class UnreadMsgResultGson extends RetResultGson{
	private UnReadMesssageDTO unReadMesssageDTO;
	public UnreadMsgResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public UnReadMesssageDTO getUnReadMesssageDTO() {
		return unReadMesssageDTO;
	}

	public void setUnReadMesssageDTO(UnReadMesssageDTO unReadMesssageDTO) {
		this.unReadMesssageDTO = unReadMesssageDTO;
	}
}
