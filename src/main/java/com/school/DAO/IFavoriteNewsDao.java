package com.school.DAO;

import com.school.Entity.FavoriteNewsDTO;

public interface IFavoriteNewsDao {
	Integer insert(FavoriteNewsDTO favoriteNewsDTO);
	void delete(FavoriteNewsDTO favoriteNewsDTO);

	FavoriteNewsDTO selectByUK(FavoriteNewsDTO favoriteNewsDTO);
}
