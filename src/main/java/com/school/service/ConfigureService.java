package com.school.service;

import com.school.AOP.CacheMethodLogo;
import com.school.Constants.RetCode;
import com.school.Constants.RetMsg;
import com.school.DAO.ISpiderEnumDao;
import com.school.Entity.CityDTO;
import com.school.Gson.CityResultGson;
import com.school.Utils.TimeUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConfigureService {
	private Logger logger = Logger.getLogger(ConfigureService.class.getName());

	@Resource
	private ISpiderEnumDao spiderEnumDao;

	@CacheMethodLogo(resTime = TimeUtils.ONE_MINUTE_SECONDS * 10)
	public CityResultGson getSupportedCities()
	{
		CityResultGson cityResultGson = new CityResultGson(RetCode.RET_CODE_OK, RetMsg.RET_MSG_OK);
		try {
			List<CityDTO> cityDTOS = spiderEnumDao.selectSupportedCities();
			cityResultGson.setCityDTOS(cityDTOS);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			cityResultGson.setResult(RetCode.RET_CODE_SYSTEMERROR, RetMsg.RET_MSG_SYSTEMERROR);
		}
		return cityResultGson;
	}
}
