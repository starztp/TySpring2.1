package com.tianyou.spring.framework.servlet;

import com.tianyou.spring.framework.annotation.TyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TyHandlerAdapter {

    public boolean supports(Object handler){
        return handler instanceof TyHandlerMapping;
    }

    public TyModelAndView handle(HttpServletRequest req, HttpServletResponse resp,Object Handler) throws InvocationTargetException, IllegalAccessException {

        TyHandlerMapping handlerMapping=(TyHandlerMapping)Handler;
        //1.把方法的形参列表和request的参数列表所在顺序进行一一对应
        Map<String,Integer> paramIndexMapping=new HashMap<>();
        //提取方法中加了注解的参数
        //把方法上的注解拿到，得到的是一个二维数组
        //因为一个参数可以有多个注解，而一个方法又有多个参数
        Annotation[][] annotations=handlerMapping.getMethod().getParameterAnnotations();
        for(int i=0;i<annotations.length;i++){
            for(Annotation annotation:annotations[i]){
                if(annotation instanceof TyRequestParam){
                    String paramname=((TyRequestParam) annotation).value();
                    if(!paramname.equals("")){
                        paramIndexMapping.put(paramname,i);
                    }
                }
            }
        }

        //提取方法中的request和response参数
        Class<?>[] classes=handlerMapping.getMethod().getParameterTypes();
        for(int i=0;i<classes.length;i++){
            if(classes[i]==HttpServletRequest.class || classes[i]==HttpServletResponse.class){
                paramIndexMapping.put(classes[i].getName(),i);
            }
        }

        //获得方法的形参列表
        //因为相同参数名称对应的参数值可能会有多个，如前端checkbox组件，所以这里是String,String[]的结构
        Map<String,String[]> params= req.getParameterMap();

        //实参列表
        Object[] paramValues=new Object[classes.length];

        //获取实际请求参数
        for (Map.Entry<String, String[]> parm : params.entrySet()) {
            String value = Arrays.toString(parm.getValue()).replaceAll("\\[|\\]","")
                    .replaceAll("\\s",",");

            if(!paramIndexMapping.containsKey(parm.getKey())){continue;}

            //获取参数所在位置
            int index = paramIndexMapping.get(parm.getKey());

            //将参数值转换成其他类型
            paramValues[index] = caseStringValue(value,classes[index]);
        }

        //将req对象加入实参列表
        if(paramIndexMapping.containsKey(HttpServletRequest.class.getName())){
            int reqIndex=paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex]=req;
        }

        //将resp对象加入实参列表
        if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }

        //方法执行逻辑
        Object result=handlerMapping.getMethod().invoke(handlerMapping.getController(),paramValues);
        if(result == null || result instanceof Void){
            return null;
        }

        boolean isModelAndView=handlerMapping.getMethod().getReturnType()==TyModelAndView.class;
        if(isModelAndView){
            return (TyModelAndView) result;
        }
            return null;
    }

    /**
     * 将String类型转换成其他任意类型
     * @param value 原String类型值
     * @param paramsType 要转换成的类型
     * @return
     */
    private Object caseStringValue(String value, Class<?> paramsType) {
        if(String.class == paramsType){
            return value;
        }
        //如果是int
        if(Integer.class == paramsType){
            return Integer.valueOf(value);
        }
        else if(Double.class == paramsType){
            return Double.valueOf(value);
        }else {
            if(value != null){
                return value;
            }
            return null;
        }
        //如果还有double或者其他类型，继续加if
        //这时候，我们应该想到策略模式了
        //在这里暂时不实现，希望小伙伴自己来实现

    }
}
