package com.minis.aop.config;

import java.lang.reflect.Method;

/**
 * 方法匹配器
 */
public interface MethodMatcher {

    boolean matches(Method method, Class<?> targetClass);
}
