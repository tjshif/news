package com.school.DAO;

import com.school.Entity.BeAdminDTO;
import org.apache.ibatis.annotations.Param;

public interface IBeAdminDao {
	BeAdminDTO selectByID(Long ID);
	BeAdminDTO selectByUserName(String userName);
	Integer updateSessionID(@Param("ID") Long ID, @Param("sessionID") String sessionID);
}
