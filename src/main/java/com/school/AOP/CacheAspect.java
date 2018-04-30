package com.school.AOP;

import com.school.Redis.StoredCacheService;
import com.school.Utils.GsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;


@Aspect
@Component
public class CacheAspect {
	Logger logger = Logger.getLogger(CacheAspect.class.getName());

	private static final String cacheKeyPrefix = "news_";

	@Resource
	protected StoredCacheService storedCacheService;

	@Around(value = "@annotation(cacheMethodLogo)")
	public Object proccessTx(ProceedingJoinPoint pjd, CacheMethodLogo cacheMethodLogo) throws Throwable {
		Method method = ((MethodSignature) pjd.getSignature()).getMethod();
		String targetName = pjd.getTarget().getClass().getName();
		String methodName = method.getName();
		Object[] arguments = pjd.getArgs();

		logger.info(String.format("proccessTx, (class=%s, method=%s, arguments=%s)", targetName, methodName, arguments));

		String annotationKey = cacheMethodLogo.cacheKey();
		boolean fixedKey = cacheMethodLogo.fixedkey();
		String cacheKey = null;
		if (fixedKey == false)
		{
			cacheKey = getCacheKey(targetName, methodName, arguments);
		}
		else
		{
			cacheKey = annotationKey;
		}

		String cacheVal = null;
		try {
			cacheVal = storedCacheService.get(cacheKey);
		}
		catch (Exception ex)
		{

		}

		Object result = null;
		if (cacheVal == null)
		{
			Object callResult = pjd.proceed();
			String gsonString = GsonUtil.toJson(callResult);

			try {
				storedCacheService.setex(cacheKey, cacheMethodLogo.resTime(), gsonString);
			}
			catch (Exception ex)
			{

			}
			return callResult;
		}
		logger.info("Gson: " + cacheVal);
		logger.info(String.format("proccessTx, calling gons.fromJson with returnType:%s", method.getReturnType()));
		result = GsonUtil.fromJson(cacheVal, method.getReturnType());

		return result;
	}

	private String getCacheKey(String targetName, String methodName, Object[] arguments)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(targetName).append(".").append(methodName);
		sb.append("(");

		String params = "";
		if ((arguments != null) && (arguments.length != 0))
		{
			StringBuffer buffer = new StringBuffer();
			for (int ii = 0; ii < arguments.length; ++ii)
			{
				if (arguments[ii] != null)
				{
					if (arguments[ii] instanceof Map)
					{
						Map map = (Map) arguments[ii];
						TreeMap treeMap = new TreeMap();
						treeMap.putAll(map);

						buffer.append(arguments[ii].getClass().getName() + "=" + treeMap.toString()).append(",");
					}
					else
					{
						buffer.append(arguments[ii].getClass().getName() + "=" + arguments[ii].toString()).append(",");
					}
				}
				else
					buffer.append("null,");
			}
			params = buffer.substring(0, buffer.length() - 1);
		}
		return cacheKeyPrefix + sb + params + ")";
	}
}
