package com.school.Redis;

import com.school.DAO.IUserDao;
import com.school.Entity.MsgDTO;
import com.school.Entity.NewsDTO;
import com.school.Entity.UserDTO;
import com.school.Enum.LocationEnum;
import com.school.Utils.GsonUtil;
import com.school.service.common.UserCommonServiceUtil;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class RedisHandler {
	private Logger logger = Logger.getLogger(RedisHandler.class.getName());

	@Resource
	private IUserDao userDao;

	@Resource
	protected StoredCacheService storedCacheService;

	@Resource
	private UserCommonServiceUtil userCommonServiceUtil;

	protected String getNewsTypeLocationKey(Integer newsType, Integer location)
	{
		if (location == null || newsType == null)
			return "";
		return String.format("type:%d;location:%d", newsType, location);
	}

	protected String getNewsTypeSubTypeLocationKey(Integer newsType, Integer subNewsType, Integer location)
	{
		if (newsType == null || location == null || subNewsType == null)
			return "";
		return String.format("type:%d;subtype:%d;loation:%d", newsType, subNewsType, location);
	}

	protected String getNewsItemKey(String id)
	{
		return "News:" + id;
	}

	protected String getNewsTypeKey(Integer newsType)
	{
		return String.format("type:%d;location:%d", newsType, LocationEnum.ALL.getZipCode());
	}

	protected String getTrimKey()
	{
		return "TRIMKEYS";
	}

	protected Long getStoredIndex(Integer newsType)
	{
		Long storedNewsIdx = -1L;
		String newsKey = getNewsTypeKey(newsType);
		if (storedCacheService.exists(newsKey))
		{
			Set<String> latestElem = storedCacheService.zrevrange(newsKey, 0, 0);
			if (latestElem != null && latestElem.size() == 1)
			{
				for (String item : latestElem)
				{
					storedNewsIdx = Long.parseLong(item);
					break;
				}
			}
		}
		return storedNewsIdx;
	}

	public void removeNewsFromRedis(NewsDTO newsItem)
	{
		if (newsItem == null)
			return;

		String key = getNewsItemKey(newsItem.getId());
		storedCacheService.del(key);

		key = getNewsTypeKey(newsItem.getNewsType());
		if (!TextUtils.isEmpty(key))
			storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());

		key = getNewsTypeLocationKey(newsItem.getNewsType(), newsItem.getLocationCode());
		storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());

		key = getNewsTypeSubTypeLocationKey(newsItem.getNewsType(), newsItem.getNewsSubType(), newsItem.getLocationCode());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zremrangeByScore(key, newsItem.getId(), newsItem.getId());
	}

	public <T extends MsgDTO> void removePostMsgFromRedis(T msgItem)
	{
		if (msgItem == null)
			return;
		//remove msgitem
		String key = getNewsItemKey(msgItem.getId());
		storedCacheService.del(key);

		//remove msg in specific location
		key  = getNewsTypeLocationKey(msgItem.getNewsType(), msgItem.getLocationCode());
		storedCacheService.zremrangeByScore(key, msgItem.getId(), msgItem.getId());

		//remove id for record(type:subtype:location)
		key = getNewsTypeSubTypeLocationKey(msgItem.getNewsType(), msgItem.getNewsSubType(), msgItem.getLocationCode());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zremrangeByScore(key, msgItem.getId(), msgItem.getId());
	}

	public <T extends MsgDTO> void loadPostMsgToRedis(T msgItem)
	{
		if (msgItem == null)
			return;

		//cache msgitem
		if (msgItem.getPublisherId() != null)
		{
			UserDTO userDTO = userCommonServiceUtil.getUserDTO(msgItem.getPublisherId());
			if (userDTO != null)
			{
				msgItem.setPublishSource(userDTO.getNickName());
			}
		}
		storedCacheService.set(getNewsItemKey(msgItem.getId()), GsonUtil.toJson(msgItem));

		/*//cache msg in all location
		storedCacheService.zadd(getNewsTypeKey(msgItem.getNewsType()), Long.parseLong(msgItem.getId()), msgItem.getId());
		storedCacheService.zadd(getTrimKey(), LocationEnum.ALL.getZipCode(), getNewsTypeKey(msgItem.getNewsType()));
*/
		//cache msg in specific location
		String key = getNewsTypeLocationKey(msgItem.getNewsType(), msgItem.getLocationCode());
		storedCacheService.zadd(key, Long.parseLong(msgItem.getId()), msgItem.getId());
		storedCacheService.zadd(getTrimKey(), msgItem.getLocationCode(), key);

		//store id for record(type:subtype:location)
		key = getNewsTypeSubTypeLocationKey(msgItem.getNewsType(), msgItem.getNewsSubType(), msgItem.getLocationCode());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zadd(key, Long.parseLong(msgItem.getId()), msgItem.getId());
		storedCacheService.zadd(getTrimKey(), msgItem.getLocationCode(), key);
	}

	public void loadNewsToRedis(NewsDTO newsItem)
	{
		if (newsItem == null)
			return;

		//cache newsItem in all location
		storedCacheService.zadd(getNewsTypeKey(newsItem.getNewsType()), Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), LocationEnum.ALL.getZipCode(), getNewsTypeKey(newsItem.getNewsType()));

		if (newsItem.getPublisherId() != null)
		{
			UserDTO userDTO = userCommonServiceUtil.getUserDTO(newsItem.getPublisherId());
			if (userDTO != null)
			{
				newsItem.setPublishSource(userDTO.getNickName());
			}
		}
		//cache newsItem
		storedCacheService.set(getNewsItemKey(newsItem.getId()), GsonUtil.toJson(newsItem));

		//cache msg in specific location
		String key = getNewsTypeLocationKey(newsItem.getNewsType(), newsItem.getLocationCode());
		if (TextUtils.isEmpty(key))
		{
			logger.error("location can't be null.");
			return;
		}

		storedCacheService.zadd(key, Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), newsItem.getLocationCode(), key);

		//store id for record(type:subtype:location)
		key = getNewsTypeSubTypeLocationKey(newsItem.getNewsType(), newsItem.getNewsSubType(), newsItem.getLocationCode());
		if (TextUtils.isEmpty(key))
			return;
		storedCacheService.zadd(key, Long.parseLong(newsItem.getId()), newsItem.getId());
		storedCacheService.zadd(getTrimKey(), newsItem.getLocationCode(), key);
	}

	protected String getSessionKey(Long adminID)
	{
		return String.format("Session:%s", adminID.toString());
	}
}
