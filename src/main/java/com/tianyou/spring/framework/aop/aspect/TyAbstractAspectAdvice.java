package com.tianyou.spring.framework.aop.aspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class TyAbstractAspectAdvice {

    private Method aspectMethod;
    private Object target;//方法所在类的实例

    public Object invokeAdviceMethod() throws InvocationTargetException, IllegalAccessException {
        Class<?>[] paramTypes=this.aspectMethod.getParameterTypes();
        if(paramTypes.length==0||paramTypes==null){
            return this.aspectMethod.invoke(target);
        }else {
            //实参列表
            Object[] args=new Object[paramTypes.length];
            for(int i=0;i<paramTypes.length;i++){
                args[i]=paramTypes[i];
            }
            return this.aspectMethod.invoke(target,args);
        }
    }
}
