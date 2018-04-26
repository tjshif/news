package com.school.DAO;


import com.school.Entity.FirstLevelCommentDTO;
import com.school.Entity.SecondLevelCommentDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICommentDao {
	List<FirstLevelCommentDTO> selectComments(@Param("newsID") Long newsID,
											  @Param("offset") Integer offset,
											  @Param("pageSize") Integer pageSize);

	List<SecondLevelCommentDTO> selectSecondLevelComments(@Param("flID") Long flID,
														  @Param("offset") Integer offset,
														  @Param("pageSize") Integer pageSize);

	Integer insertFirstLevelComment(FirstLevelCommentDTO firstLevelCommentDTO);
	Integer insertSecondLevelComment(SecondLevelCommentDTO secondLevelCommentDTO);

	void increaseCommentCount(Long ID);
	void decreaseCommentCount(Long ID);

	Integer deleteComment(Long ID);
	int deleteReplyComment(Long ID);

	SecondLevelCommentDTO selectSLCommentByID(Long ID);
}
