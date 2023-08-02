package com.minis.aop.factory;

import com.minis.aop.advisor.Advisor;
import com.minis.aop.proxy.AopProxy;
import com.minis.aop.proxy.JdkDynamicAopProxy;

/**
 * 默认 AOP 代理工厂
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target, Advisor advisor) {
        return new JdkDynamicAopProxy(target, advisor);
    }
}
