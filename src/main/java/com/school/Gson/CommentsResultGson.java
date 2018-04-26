package com.school.Gson;

import com.school.Entity.FirstLevelCommentDTO;

import java.util.List;

public class CommentsResultGson extends RetResultGson {
	private List<FirstLevelCommentDTO> firstLevelCommentDTOS;

	public CommentsResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public void setFirstLevelCommentDTOS(List<FirstLevelCommentDTO> firstLevelCommentDTOS) {
		this.firstLevelCommentDTOS = firstLevelCommentDTOS;
	}

	public List<FirstLevelCommentDTO> getFirstLevelCommentDTOS() {
		return firstLevelCommentDTOS;
	}
}
