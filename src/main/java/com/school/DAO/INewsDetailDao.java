package com.school.DAO;

import com.school.Entity.NewsDetailDTO;
import com.school.Gson.NewsDetailGson;

public interface INewsDetailDao {
	Integer insert(NewsDetailDTO newsDetailDTO);

	NewsDetailDTO selectNewsDetail(Long newsID);
	Integer update(NewsDetailGson newsDetailGson);

	Long selectNewsDetailIDByUrl(String sourceArticleUrl);
}
