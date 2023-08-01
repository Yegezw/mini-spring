package com.minis.aop.proxy;

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

    public JdkDynamicAopProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object getProxy() {
        Object obj = Proxy.newProxyInstance(
                JdkDynamicAopProxy.class.getClassLoader(),
                target.getClass().getInterfaces(),
                this
        );
        System.out.println("---------- In JdkDynamicAopProxy.getProxy(), new proxy instance created for: " + obj);
        return obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 只对 doAction() 添加额外的逻辑
        if (method.getName().equals("doAction")) {
            System.out.println("---------- before call real object, dynamic proxy ----------");
        }

        return method.invoke(target, args);
    }
}
