package com.minis.aop.proxy;

import com.minis.aop.advice.interceptor.MethodInterceptor;
import com.minis.aop.method.MethodInvocation;
import com.minis.aop.method.ReflectiveMethodInvocation;
import com.minis.aop.advisor.Advisor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK 动态代理
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    /**
     * 目标对象
     */
    private Object target;
    /**
     * 增强器
     */
    private Advisor advisor;

    public JdkDynamicAopProxy(Object target, Advisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(
                JdkDynamicAopProxy.class.getClassLoader(),
                target.getClass().getInterfaces(),
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 只对 doAction() 添加额外的逻辑
        if (method.getName().equals("doAction")) {
            Class<?> targetClass = (target != null ? target.getClass() : null);
            MethodInterceptor interceptor = advisor.getMethodInterceptor();
            MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);

            return interceptor.invoke(invocation);
        }

        return null;
    }
}
