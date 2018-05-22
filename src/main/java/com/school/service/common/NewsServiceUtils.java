package com.school.service.common;

import com.school.AOP.CacheMethodLogo;
import com.school.DAO.INewsDao;
import com.school.DAO.INewsDetailDao;
import com.school.DAO.IVisitCountDao;
import com.school.Entity.MsgAggregate;
import com.school.Entity.NewsDTO;
import com.school.Gson.CounterMsgGson;
import com.school.Utils.TimeUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
public class NewsServiceUtils {
	@Resource
	private INewsDao newsDao;

	@Resource
	private INewsDetailDao newsDetailDao;

	@Resource
	private IVisitCountDao visitCountDao;

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS)
	public NewsDTO getNews(Long newsID)
	{
		if (newsID == null)
			return null;

		return newsDao.selectNewsById(newsID);
	}

	@Transactional
	public void saveMsgToDB(MsgAggregate msgAggregate)
	{
		if (msgAggregate.getNewsDTO() != null)
			newsDao.insert(msgAggregate.getNewsDTO());

		if (msgAggregate.getNewsDetailDTO() != null)
			newsDetailDao.insert(msgAggregate.getNewsDetailDTO());
	}

	@Transactional
	public void increaseVisitCount(CounterMsgGson counterMsgGson)
	{
		if (counterMsgGson == null)
			return;
		visitCountDao.increase(counterMsgGson.getNewsID());
	}
}
