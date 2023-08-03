package com.minis.aop.factory;

import com.minis.aop.advisor.PointcutAdvisor;
import com.minis.aop.proxy.AopProxy;
import com.minis.aop.proxy.JdkDynamicAopProxy;

/**
 * 默认 AOP 代理工厂
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target, PointcutAdvisor advisor) {
        return new JdkDynamicAopProxy(target, advisor);
    }
}
