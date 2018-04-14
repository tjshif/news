package com.school.DAO;

import com.school.Entity.FavoriteNewsDTO;
import com.school.Entity.NewsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IFavoriteNewsDao {
	Integer insert(FavoriteNewsDTO favoriteNewsDTO);
	void delete(FavoriteNewsDTO favoriteNewsDTO);

	FavoriteNewsDTO selectByUK(FavoriteNewsDTO favoriteNewsDTO);

	List<NewsDTO> selectNewsByUserID(@Param("userID")Long userID,
									 @Param("offset")Long offset,
									 @Param("pageSize")Integer pageSize);
}
