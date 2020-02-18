package com.tianyou.spring.framework.context;

import com.tianyou.spring.framework.annotation.TyAutowired;
import com.tianyou.spring.framework.beans.TyBeanWrapper;
import com.tianyou.spring.framework.beans.config.TyBeanDefinition;
import com.tianyou.spring.framework.beans.config.TyBeanPostProcessor;
import com.tianyou.spring.framework.beans.support.TyBeanDefinitionReader;
import com.tianyou.spring.framework.beans.support.TyDefaultListableBeanFactory;
import com.tianyou.spring.framework.core.BeanFactory;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TyApplicationContext extends TyDefaultListableBeanFactory implements BeanFactory {

    private String configlocation;

    //通用IOC容器
    private Map<String,TyBeanWrapper> factoryBeanInstanceCache=new ConcurrentHashMap<>();

    public TyApplicationContext(String localtion){

        this.configlocation=localtion;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取所有在IOC容器中的BeanName
    public String[] getBeanInstanceKeySet(){
        return this.factoryBeanInstanceCache.keySet().toArray(new String[]{});
    }



    @Override
    public Object getBean(String beanname) throws Exception {
        Object instance=null;
        TyBeanPostProcessor beanPostProcessor=new TyBeanPostProcessor();
        beanPostProcessor.postProcessBeforeInitialization(instance,beanname);
        instance=initBeanInstance(beanDefinitionMap.get(beanname));
        beanPostProcessor.postProcessAfterInitialization(instance,beanname);
        TyBeanWrapper beanWrapper=new TyBeanWrapper(instance);
        factoryBeanInstanceCache.put(beanname,beanWrapper);
        populateBean(beanWrapper);
        return factoryBeanInstanceCache.get(beanname).getInstance();
    }

    /**
     * 定位，加载，注册
     */
    public void refresh()throws Exception {
        //1.定位,扫描配置文件中的包路径
        TyBeanDefinitionReader reader=new TyBeanDefinitionReader(this.configlocation);

        //2.加载配置文件，封装成BeanDefinition
        List<TyBeanDefinition> beanDefinitionList=reader.loadBeanDefinition();

        //3.将封装好的BeanDefinition添加到容器中
        registBeanDefinition(beanDefinitionList);

    }


    private void registBeanDefinition(List<TyBeanDefinition> beanDefinitions){
        for(TyBeanDefinition beanDefinition:beanDefinitions){
            super.beanDefinitionMap.put(beanDefinition.getBeanClassName(),beanDefinition);
        }
    }

    private Object initBeanInstance(TyBeanDefinition beanDefinition) throws Exception {
        Class<?> clazz=Class.forName(beanDefinition.getBeanClassName());
        return clazz.newInstance();
    }

    private void populateBean(TyBeanWrapper beanWrapper){
        Object instance=beanWrapper.getInstance();
        Field[] fields=beanWrapper.getClazz().getFields();
        for(Field field:fields){
            //强行访问private修饰的字段
            field.setAccessible(true);
            if(!field.isAnnotationPresent(TyAutowired.class)){
                continue;
            }
            TyAutowired autowired=field.getAnnotation(TyAutowired.class);
            String autowiredbeanname=autowired.value().trim();
            if(autowiredbeanname.equals("")){
                autowiredbeanname=field.getType().getName();
            }
            if(this.factoryBeanInstanceCache.get(autowiredbeanname)==null){
                continue;
            }
            try {
                field.set(instance,this.factoryBeanInstanceCache.get(autowiredbeanname).getInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
