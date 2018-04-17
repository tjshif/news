package com.school.DAO;

import com.school.Entity.UserDTO;

public interface IUserDao {
	Integer insert(UserDTO userDTO);
	UserDTO selectByPhoneNo(String username);
	UserDTO selectByID(Long id);
}
