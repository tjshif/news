package com.school.Entity;

public class BookDTO {
	private String		ID;
	private String 	book_name;
	private Integer 	grade;
	private String  	image;

	public String getId() {
		return ID;
	}

	public void setId(String id) {
		this.ID = id;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public String getImage() {
		return image;
	}

	public String getBook_name() {
		return book_name;
	}

	public Integer getGrade() {
		return grade;
	}
}
