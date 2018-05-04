package com.school.DAO;

import com.school.Entity.UnReadMesssageDTO;

public interface IUnReadMesssageDao {
	Integer insert(UnReadMesssageDTO unReadMesssageDTO);
	Integer clearUnRead(Long userID);

	UnReadMesssageDTO selectByUserID(Long userID);
}
