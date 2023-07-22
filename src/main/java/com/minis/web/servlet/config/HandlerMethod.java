package com.minis.web.servlet.config;

import java.lang.reflect.Method;

/**
 * 处理器方法, 包含 Controller + Method
 */
public class HandlerMethod {

    private Object bean;
    private Class<?> beanType;
    private Method method;
    private MethodParameter[] parameters;
    private Class<?> returnType;
    private String description;
    private String className;
    private String methodName;

    public HandlerMethod(Object obj, Method method) {
        setBean(obj);
        setMethod(method);
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
