package com.minis.aop.method;

import java.lang.reflect.Method;

/**
 * 方法调用
 */
public interface MethodInvocation {

    /**
     * 获取方法
     */
    Method getMethod();

    /**
     * 获取参数
     */
    Object[] getArguments();

    /**
     * 获取对象
     */
    Object getThis();

    /**
     * 执行方法
     */
    Object proceed() throws Throwable;
}
