package com.tianyou.spring.framework.beans.support;

import com.tianyou.spring.framework.beans.config.TyBeanDefinition;
import com.tianyou.spring.framework.core.BeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TyDefaultListableBeanFactory implements BeanFactory {


    //存储BeanDefinition的容器
    protected final Map<String,TyBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,TyBeanDefinition>();

    @Override
    public Object getBean(String beanname) throws Exception {
        return null;
    }
}
