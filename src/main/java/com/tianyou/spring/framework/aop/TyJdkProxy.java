package com.tianyou.spring.framework.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TyJdkProxy implements TyAopProxy,InvocationHandler {
    @Override
    public Object getproxy() {
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
