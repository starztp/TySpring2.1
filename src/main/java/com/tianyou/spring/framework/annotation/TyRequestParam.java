package com.tianyou.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 请求参数映射
 * @author Tom
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TyRequestParam {
	
	String value() default "";
	
	boolean required() default true;

}
