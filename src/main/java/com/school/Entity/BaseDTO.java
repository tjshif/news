package com.school.Entity;

import java.sql.Timestamp;

public class BaseDTO {
	private String		ID;
	private Timestamp  updateAt;
	private String		updateBy;
	private Timestamp	createAt;
	private String		createBy = "sys";

	public String getId() {
		return ID;
	}
	public void setId(String id)
	{
		this.ID = id;
	}

	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Timestamp getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	public Timestamp getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}
}
