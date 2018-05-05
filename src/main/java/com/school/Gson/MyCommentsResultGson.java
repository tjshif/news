package com.school.Gson;

import com.school.Entity.FirstLevelCommentDTO;

import java.util.List;

public class MyCommentsResultGson extends RetResultGson {
	private List<FirstLevelCommentDTO> firstLevelCommentDTOS;

	public MyCommentsResultGson(int retCode, String message)
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
