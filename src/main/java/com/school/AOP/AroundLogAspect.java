package com.school.AOP;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AroundLogAspect {
	Logger logger = Logger.getLogger(AroundLogAspect.class.getName());

	@Around(value = "@annotation(log)")
	public Object aroundLogAnnotaion(ProceedingJoinPoint pjd, LogAnnotation log)
	{
		Object[] args = pjd.getArgs();
		Object result = null;
		logger.info("@Around:before run method:" + pjd.getSignature().getName() + "\nparams = " + Arrays.toString(args));
		try {
			result = pjd.proceed();;
			logger.info("result: " + result);
		}
		catch (Throwable throwable) {
			logger.error(throwable.getMessage());
		}

		logger.info("@Around:end run method:"+ pjd.getSignature().getName());
		return result;
	}
}
