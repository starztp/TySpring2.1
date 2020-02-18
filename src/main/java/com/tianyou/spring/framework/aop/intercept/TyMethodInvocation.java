package com.tianyou.spring.framework.aop.intercept;

import java.lang.reflect.Method;
import java.util.List;

public class TyMethodInvocation {

    private Object proxy;//代理对象
    private Object target;//目标对象
    private Method method;
    private Object[] args;
    private Class<?> targetclazz;//目标类
    private List<Object> interceptorsAndDynamicMethodMatchers; //执行器链
    //定义一个索引，从-1开始来记录当前拦截器执行的位置
    private int currentInterceptorIndex = -1;

    public Object proceed() throws Throwable{
        //如果通知都执行好了就执行方法本身
        if(interceptorsAndDynamicMethodMatchers.size()-1==currentInterceptorIndex){
            this.method.invoke(target,args);
        }

        //如果执行器链没有执行完，则获取执行器链中的通知
        //每次执行时，计数器+1
        Object advice=interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if(advice instanceof TyMethodInterceptor){
            advice=(TyMethodInterceptor)advice;
            return ((TyMethodInterceptor) advice).invoke(this);
        }else {
            return proceed();
        }
    }



}
