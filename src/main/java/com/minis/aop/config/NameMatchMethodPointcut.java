package com.minis.aop.config;

import com.minis.util.PatternMatchUtils;

import java.lang.reflect.Method;

/**
 * 名称匹配方法切入点
 */
public class NameMatchMethodPointcut implements MethodMatcher, Pointcut {

    private String mappedName = "";

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    /**
     * 核心方法, 判断方法名是否匹配给定的模式
     */
    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }
}
