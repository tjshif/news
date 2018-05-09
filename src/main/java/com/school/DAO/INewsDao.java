package com.school.DAO;

import com.school.Entity.NewsDTO;
import com.school.Gson.NewsGson;
import org.apache.ibatis.annotations.Param;

import javax.ws.rs.PathParam;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface INewsDao {
	List<NewsDTO> selectNewsByCreateAt(Date createDay);

	List<NewsDTO> selectNewsByCreateAtFromID(@Param(value = "createDay")Date createDay, @Param(value = "fromID")Long fromID);

	List<NewsDTO> selectNewsGreaterThanId(@Param("newsType") Integer newsType, @Param("newsSubType") Integer newsSubType,
								   @Param("locationCode") Integer locationCode, @Param("ID") Long ID,
								   @Param("count")Integer count);

	List<NewsDTO> selectNewsLessThanId(@Param("newsType") Integer newsType, @Param("newsSubType") Integer newsSubType,
										  @Param("locationCode") Integer locationCode, @Param("ID") Long ID,
										  @Param("count")Integer count);

	List<NewsDTO> selectNewsByPage(@Param("newsType") Integer newsType, @Param("newsSubType") Integer newsSubType,
									   @Param("locationCode") Integer locationCode, @Param("offset") Long offset,
									   @Param("pageSize")Integer pageSize);

	NewsDTO selectNewsById(Long ID);

	int updateNewsStatus(@Param("ID") Long ID, @Param("isValid") Boolean isValid);

	Integer getCount(@Param("newsType") Integer newsType, @Param("newsSubType") Integer newsSubType,
					 @Param("locationCode") Integer locationCode);

	Integer update(NewsGson newsGson);
}
