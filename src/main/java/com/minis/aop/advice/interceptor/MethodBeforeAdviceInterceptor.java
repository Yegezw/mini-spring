package com.minis.aop.advice.interceptor;

import com.minis.aop.advice.before.BeforeAdvice;
import com.minis.aop.advice.before.MethodBeforeAdvice;
import com.minis.aop.method.MethodInvocation;

public class MethodBeforeAdviceInterceptor implements MethodInterceptor, BeforeAdvice {

    private final MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        return methodInvocation.proceed();
    }
}
