package com.minis.aop.advisor;

import com.minis.aop.advice.interceptor.MethodInterceptor;

/**
 * 默认增强器
 */
public class DefaultAdvisor implements Advisor {

    private MethodInterceptor methodInterceptor;

    public DefaultAdvisor() {
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }
}
