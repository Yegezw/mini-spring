package com.minis.aop.advice.interceptor;

import com.minis.aop.advice.after.AfterAdvice;
import com.minis.aop.advice.after.AfterReturningAdvice;
import com.minis.aop.method.MethodInvocation;

public class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice {

	private final AfterReturningAdvice advice;

	public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
		this.advice = advice;
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object retVal = methodInvocation.proceed();
		advice.afterReturning(retVal, methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
		return retVal;
	}
}
