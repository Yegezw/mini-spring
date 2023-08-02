package com.minis.aop.advisor;

import com.minis.aop.advice.interceptor.MethodInterceptor;

/**
 * 增强器
 */
public interface Advisor {

    MethodInterceptor getMethodInterceptor();

    void setMethodInterceptor(MethodInterceptor methodInterceptor);
}
