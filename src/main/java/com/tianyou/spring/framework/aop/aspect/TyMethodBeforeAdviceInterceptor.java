package com.tianyou.spring.framework.aop.aspect;

import com.tianyou.spring.framework.aop.intercept.TyMethodInterceptor;
import com.tianyou.spring.framework.aop.intercept.TyMethodInvocation;

public class TyMethodBeforeAdviceInterceptor implements TyMethodInterceptor {
    @Override
    public Object invoke(TyMethodInvocation invocation) throws Throwable {
        return null;
    }

    public void advicebefore(TyMethodInvocation invocation) throws Throwable {

    }
}
