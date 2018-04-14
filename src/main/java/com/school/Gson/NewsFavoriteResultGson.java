package com.school.Gson;

public class NewsFavoriteResultGson extends RetResultGson {
	private Boolean isFavorite;

	public NewsFavoriteResultGson(int retCode, String message)
	{
		super(retCode, message);
	}

	public void setFavorite(Boolean favorite) {
		isFavorite = favorite;
	}

}
