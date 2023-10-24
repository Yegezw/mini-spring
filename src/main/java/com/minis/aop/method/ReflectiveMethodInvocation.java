package com.minis.aop.method;

import java.lang.reflect.Method;

/**
 * 反射方法调用
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    /**
     * 代理对象
     */
    protected final Object proxy;
    /**
     * 目标对象
     */
    protected final Object target;
    /**
     * 方法
     */
    protected final Method method;
    /**
     * 方法参数
     */
    protected Object[] arguments;
    /**
     * 目标对象 Class
     */
    private Class<?> targetClass;
    // protected MethodInterceptor methodInterceptor;

    public ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass) {
        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        // this.methodInterceptor = methodInterceptor;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }

    // ======================================== getter() ========================================

    public final Object getProxy() {
        return proxy;
    }

    @Override
    public final Object getThis() {
        return target;
    }

    @Override
    public final Method getMethod() {
        return method;
    }

    @Override
    public final Object[] getArguments() {
        return arguments;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    // ======================================== setter() ========================================

    public void setArguments(Object... arguments) {
        this.arguments = arguments;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }
}
