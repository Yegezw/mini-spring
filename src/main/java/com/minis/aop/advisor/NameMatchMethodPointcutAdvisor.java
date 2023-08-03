package com.minis.aop.advisor;

import com.minis.aop.advice.Advice;
import com.minis.aop.advice.after.AfterAdvice;
import com.minis.aop.advice.after.AfterReturningAdvice;
import com.minis.aop.advice.before.BeforeAdvice;
import com.minis.aop.advice.before.MethodBeforeAdvice;
import com.minis.aop.advice.interceptor.AfterReturningAdviceInterceptor;
import com.minis.aop.advice.interceptor.MethodBeforeAdviceInterceptor;
import com.minis.aop.advice.interceptor.MethodInterceptor;
import com.minis.aop.config.NameMatchMethodPointcut;
import com.minis.aop.config.Pointcut;

/**
 * 名称匹配方法切入点增强器
 */
public class NameMatchMethodPointcutAdvisor implements PointcutAdvisor {

    private Advice advice = null;
    private MethodInterceptor methodInterceptor;
    private String mappedName;
    private final NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();

    public NameMatchMethodPointcutAdvisor() {
    }

    public NameMatchMethodPointcutAdvisor(Advice advice) {
        this.advice = advice;
    }

    // ======================================== getter()、setter() ========================================

    public void setAdvice(Advice advice) {
        this.advice = advice;
        MethodInterceptor methodInterceptor = null;

        if (advice instanceof BeforeAdvice) {
            methodInterceptor = new MethodBeforeAdviceInterceptor((MethodBeforeAdvice) advice);
        } else if (advice instanceof AfterAdvice) {
            methodInterceptor = new AfterReturningAdviceInterceptor((AfterReturningAdvice) advice);
        } else if (advice instanceof MethodInterceptor) {
            methodInterceptor = (MethodInterceptor) advice;
        }

        setMethodInterceptor(methodInterceptor);
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
        this.pointcut.setMappedName(mappedName);
    }
}
