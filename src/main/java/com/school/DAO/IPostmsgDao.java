package com.school.DAO;

import com.school.Entity.PostmsgDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IPostmsgDao {
	Integer insert(PostmsgDTO postmsgDTO);

	List<PostmsgDTO> selectPostmsgByPage(@Param("newsType") Integer newsType, @Param("newsSubType") Integer newsSubType,
										  @Param("locationCode") Integer locationCode, @Param("offset") Long offset,
										  @Param("pageSize")Integer pageSize);
}
