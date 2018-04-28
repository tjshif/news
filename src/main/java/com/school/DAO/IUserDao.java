package com.school.DAO;

import com.school.Entity.UserDTO;
import org.apache.ibatis.annotations.Param;

public interface IUserDao {
	Integer insert(UserDTO userDTO);
	UserDTO selectByPhoneNo(String username);
	UserDTO selectByID(Long id);

	Integer updateNickName(@Param("ID") Long ID, @Param("nickName") String nickName);
	Integer updateSex(@Param("ID") Long ID, @Param("sex") Boolean sex);
	Integer updateCollege(@Param("ID") Long ID, @Param("college") String college);
	Integer updateAvatarUrl(@Param("ID") Long ID, @Param("avatarUrl") String avatarUrl);
}
