package com.minis.aop.advice.interceptor;

import com.minis.aop.method.MethodInvocation;

/**
 * 方法拦截器
 */
public interface MethodInterceptor extends Interceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;
}
