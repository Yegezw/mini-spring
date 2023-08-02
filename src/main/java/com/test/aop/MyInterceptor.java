package com.test.aop;

import com.minis.aop.advice.interceptor.MethodInterceptor;
import com.minis.aop.method.MethodInvocation;

import java.util.Arrays;

public class MyInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println(
                "method " + invocation.getMethod() + " is called on " + invocation.getThis() + 
                " with args " + Arrays.toString(invocation.getArguments())
        );
        Object ret = invocation.proceed();
        System.out.println("method " + invocation.getMethod() + " returns " + ret);

        return ret;
    }
}
