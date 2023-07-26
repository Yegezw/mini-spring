package com.minis.web.servlet.config;

import java.lang.reflect.Method;

/**
 * 处理器方法, 包含 Controller + Method
 */
public class HandlerMethod {

    public Object bean;
    public Class<?> beanType;
    public Method method;
    public MethodParameter[] parameters;
    public Class<?> returnType;
    public String description;
    public String className;
    public String methodName;

    public HandlerMethod(Object obj, Class<?> clazz, Method method, String methodName) {
        this.bean = obj;
        this.beanType = clazz;
        this.method = method;
        this.methodName = methodName;
    }
}
