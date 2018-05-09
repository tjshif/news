package com.school.DAO;

import com.school.Entity.NewsDetailDTO;
import com.school.Gson.NewsDetailGson;

public interface INewsDetailDao {
	NewsDetailDTO selectNewsDetail(Long newsID);
	Integer update(NewsDetailGson newsDetailGson);
}
