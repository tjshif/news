package com.school.DAO;

import com.school.Entity.NewsDTO;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface INewsDao {
	List<NewsDTO> selectNewsByCreateAt(Date createDay);

	List<NewsDTO> selectNewsByCreateAtFromID(@Param(value = "createDay")Date createDay, @Param(value = "fromID")Long fromID);

	List<NewsDTO> selectNewsGreaterThanId(@Param("newsType") Integer newsType, @Param("newsSubEnum") Integer newsSubEnum,
								   @Param("location") Integer location, @Param("id") Long id,
								   @Param("count")Integer count);

	List<NewsDTO> selectNewsLessThanId(@Param("newsType") Integer newsType, @Param("newsSubEnum") Integer newsSubEnum,
										  @Param("location") Integer location, @Param("id") Long id,
										  @Param("count")Integer count);
}
