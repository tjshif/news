package com.school.DAO;

import com.school.Entity.NewsDTO;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface INewsDao {
	List<NewsDTO> selectNewsByCreateAt(Date createDay);
	List<NewsDTO> selectNewsByCreateAtFromID(@Param(value = "createDay")Date createDay, @Param(value = "fromID")Long fromID);
}
