package com.minis.aop.advice.before;

import java.lang.reflect.Method;

public interface MethodBeforeAdvice extends BeforeAdvice {

    void before(Method method, Object[] args, Object target) throws Throwable;
}
