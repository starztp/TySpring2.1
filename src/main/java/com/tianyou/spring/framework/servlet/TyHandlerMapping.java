package com.tianyou.spring.framework.servlet;

import java.lang.reflect.Method;

public class TyHandlerMapping {

    private Method method;
    private Object Controller;
    private String url;

    public TyHandlerMapping(Method method, Object controller, String url) {
        this.method = method;
        Controller = controller;
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public Object getController() {
        return Controller;
    }

    public String getUrl() {
        return url;
    }
}
