package com.school.Gson;

import com.school.Entity.ReplymeCommentDTO;

import java.util.List;

public class ReplymeResultGson extends RetResultGson {
	private List<ReplymeCommentDTO> replymeCommentDTOS;

	public ReplymeResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public List<ReplymeCommentDTO> getReplymeCommentDTOS() {
		return replymeCommentDTOS;
	}

	public void setReplymeCommentDTOS(List<ReplymeCommentDTO> replymeCommentDTOS) {
		this.replymeCommentDTOS = replymeCommentDTOS;
	}
}
