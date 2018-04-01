package com.school.DAO;

import com.school.Entity.NewsDTO;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface INewsDao {
	List<NewsDTO> selectNewsByCreateAt(Date createDay);
}
