package com.school.DAO;

import com.school.Entity.NewsDetailDTO;

public interface INewsDetailDao {
	NewsDetailDTO selectNewsDetail(Long newsID);
}
