package com.school.DAO;

import com.school.Entity.FocusDTO;
import org.apache.ibatis.annotations.Param;

import javax.ws.rs.FormParam;
import java.util.List;

public interface IFocusDao {
	List<FocusDTO> selectFocusByUserID(@Param("fromUserID") String fromUserID, @Param("offset") Long offset,
									   @Param("pageSize")Integer pageSize);

	FocusDTO selectFocus(@Param("fromUserID") String fromUserID, @Param("toUserID") String toUserID);

	FocusDTO selectByID(String ID);

	Integer insert(FocusDTO focusDTO);

	Integer update(FocusDTO focusDTO);
}
