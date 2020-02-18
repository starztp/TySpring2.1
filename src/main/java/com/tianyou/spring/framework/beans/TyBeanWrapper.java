package com.tianyou.spring.framework.beans;

public class TyBeanWrapper {
    private Object instance;//bean的实例
    private Class<?> clazz;//bean的class

    public TyBeanWrapper(Object instance){
        this.instance=instance;
    }

    public Object getInstance() {
        return instance;
    }

    public Class<?> getClazz() {
        return this.instance.getClass();
    }

}
