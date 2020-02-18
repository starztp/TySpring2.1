package com.tianyou.spring.framework.aop.intercept;

public interface TyMethodInterceptor {

    Object invoke(TyMethodInvocation invocation) throws Throwable;
}
