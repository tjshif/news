package com.school.Entity;

public class FeedbackDTO extends BaseDTO {
	private String contantInfo;
	private String feedback;

	public FeedbackDTO()
	{

	}

	public FeedbackDTO(String contantInfo, String feedback)
	{
		this.contantInfo = contantInfo;
		this.feedback = feedback;
	}

}
