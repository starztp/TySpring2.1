package com.tianyou.spring.framework.servlet;

import com.sun.java.util.jar.pack.Package;
import com.tianyou.spring.framework.annotation.TyController;
import com.tianyou.spring.framework.annotation.TyRequestMapping;
import com.tianyou.spring.framework.context.TyApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TyDispatcherServlet extends HttpServlet {

    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private List<TyHandlerMapping> handlerMappingList=new ArrayList<TyHandlerMapping>();

    private Map<TyHandlerMapping,TyHandlerAdapter> handleradapters=new HashMap<TyHandlerMapping,TyHandlerAdapter>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        TyApplicationContext context=new TyApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        initStrategies(context);
    }



    //初始化策略
    protected void initStrategies(TyApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);
        
        //初始化本地语言环境
        initLocaleResolver(context);
        
        //初始化模板处理器
        initThemeResolver(context);

        //handlerMapping，必须实现
        initHandlerMappings(context);
        
        //初始化参数适配器，必须实现
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);


        //初始化视图转换器，必须实现
        initViewResolvers(context);
        //参数缓存器
        initFlashMapManager(context);
    }

    private void initMultipartResolver(TyApplicationContext context) {
    }

    private void initLocaleResolver(TyApplicationContext context) {
    }

    private void initThemeResolver(TyApplicationContext context) {
    }

    private void initHandlerMappings(TyApplicationContext context) throws Exception {
        String[] beannames=context.getBeanInstanceKeySet();
        for(String beanname:beannames){
            Class<?> clazz=Class.forName(beanname);
            if(!clazz.isAnnotationPresent(TyController.class)){
                continue;
            }

            if(!clazz.isAnnotationPresent(TyController.class)){
                continue;
            }
            Method[] methods=clazz.getMethods();
            for(Method method:methods){
                if(!method.isAnnotationPresent(TyRequestMapping.class)){
                    continue;
                }
                TyRequestMapping methodrequestMapping=method.getAnnotation(TyRequestMapping.class);
                TyRequestMapping classrequestMapping=clazz.getAnnotation(TyRequestMapping.class);
                String methodurl=methodrequestMapping.value().trim();
                String baseurl=classrequestMapping.value()+methodurl;
                handlerMappingList.add(new TyHandlerMapping(method,clazz,baseurl));
            }

        }

    }

    private void initHandlerAdapters(TyApplicationContext context) {
        for(TyHandlerMapping handlerMapping:handlerMappingList){
            handleradapters.put(handlerMapping,new TyHandlerAdapter());
        }
    }

    private void initHandlerExceptionResolvers(TyApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(TyApplicationContext context) {
    }

    private void initViewResolvers(TyApplicationContext context) {
    }

    private void initFlashMapManager(TyApplicationContext context) {
    }

    private void doDispatch(HttpServletRequest req,HttpServletResponse resp){
        //1.从Request中获取URL，去映射一个HandlerMapping
        String url=req.getRequestURI();
        for(TyHandlerMapping handlerMapping:handlerMappingList){
            if(!url.equals(handlerMapping.getUrl())){
                continue;
            }
            //2.获取HandlerAdapter
            TyHandlerAdapter adapter=getAdapter(handlerMapping);

            TyModelAndView modelAndView=adapter.handle(req,resp,handlerMapping);

            //将modelandview转化为html页面
            processDispatchResult(req, resp, modelAndView);

        }

        //3.真正调用方法

        //4.将modelandview转化为html页面

    }

    private TyHandlerAdapter getAdapter(TyHandlerMapping handlerMapping){
        return handleradapters.get(handlerMapping);
    }

}
