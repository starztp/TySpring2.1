package com.tianyou.spring.framework.beans.support;


import com.tianyou.spring.framework.beans.config.TyBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TyBeanDefinitionReader {

    private Properties config=new Properties();

    //写死application.properties中的包路径Key
    private final String propertykey="scanPackage";

    //用于保存类名的容器
    List<String> classnames=new ArrayList<String>();

    /**
     *
     * @param location 配置文件路径
     */
    public TyBeanDefinitionReader(String location) throws Exception {

        //将配置文件中的信息转换成inputstream对象
        InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream(location);
        config.load(inputStream);
        doScanner(config.getProperty(propertykey));
    }

    /**
     * 扫描包路径，并将包路径下的class文件名称存储到容器中
     * @param packagePath 配置文件中的包路径
     */
    private void doScanner(String packagePath){
        //将配置文件中的包路径替换成文件路径
        URL url = this.getClass().getResource("/" + packagePath.replaceAll("\\.","/"));
        File PackagePath=new File(url.getFile());
        for(File file:PackagePath.listFiles()){
            if(file.isDirectory()){
               doScanner(file.getPath());
            }

            //保证class结尾的文件的文件名都添加到容器中
            if(file.getName().endsWith(".class")){
                String classname = file.getName().replace(".class", "");
                classnames.add(classname);
            }
        }
    }

    public List<TyBeanDefinition> loadBeanDefinition() throws ClassNotFoundException {
        List<TyBeanDefinition> BeanDefinitions=new ArrayList<TyBeanDefinition>();
        for(String classname:classnames){
           Class<?> clazz= Class.forName(classname);
            TyBeanDefinition beanDefinition=doCreateBeanDefinition(classname);
            BeanDefinitions.add(beanDefinition);

            for(Class<?> interfaze:clazz.getInterfaces()){
                BeanDefinitions.add(doCreateBeanDefinition(interfaze.getName()));
            }
        }
        return BeanDefinitions;
    }

    private TyBeanDefinition doCreateBeanDefinition(String classname){
        try {
            if(Class.forName(classname).isInterface()){
                return null;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        TyBeanDefinition beanDefinition=new TyBeanDefinition();
        beanDefinition.setBeanClassName(classname);
        return beanDefinition;
    }
}
