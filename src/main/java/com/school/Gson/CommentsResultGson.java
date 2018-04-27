package com.school.Gson;

import com.school.Entity.FirstLevelCommentDTO;

import java.util.List;

public class CommentsResultGson extends RetResultGson {
	private List<FirstLevelCommentDTO> firstLevelCommentDTOS;
	private Integer totoalCount;
	private Boolean hasMore;

	public CommentsResultGson(int retCode, String message)
	{
		super(retCode, message);
		setTotoalCount(0);
		setHasMore(true);
	}

	public void setFirstLevelCommentDTOS(List<FirstLevelCommentDTO> firstLevelCommentDTOS) {
		this.firstLevelCommentDTOS = firstLevelCommentDTOS;
	}

	public List<FirstLevelCommentDTO> getFirstLevelCommentDTOS() {
		return firstLevelCommentDTOS;
	}

	public Integer getTotoalCount() {
		return totoalCount;
	}

	public void setTotoalCount(Integer totoalCount) {
		this.totoalCount = totoalCount;
	}

	public Boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
	}
}
