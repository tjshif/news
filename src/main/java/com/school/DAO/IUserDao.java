package com.school.DAO;

import com.school.Entity.UserDTO;
import com.school.Gson.UserInfoGson;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface IUserDao {
	Integer insert(UserDTO userDTO);
	UserDTO selectByPhoneNo(String phoneNo);
	UserDTO selectByID(String ID);
	UserDTO selectByNickName(String nickName);
	List<UserDTO> selectUsers(@Param("set") Set<String> userIDs);

	Integer updateNickName(@Param("ID") String ID, @Param("nickName") String nickName);
	Integer updateSex(@Param("ID") String ID, @Param("sex") Boolean sex);
	Integer updateCollege(@Param("ID") String ID, @Param("college") String college);
	Integer updateAvatarUrl(@Param("ID") String ID, @Param("avatarUrl") String avatarUrl);
	Integer updateUserInfo(UserInfoGson userInfoGson);
}
