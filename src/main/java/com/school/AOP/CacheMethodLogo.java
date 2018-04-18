package com.school.AOP;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheMethodLogo {
	String cacheKey() default "";

	int resTime() default 100;

	boolean fixedkey() default false;

}
