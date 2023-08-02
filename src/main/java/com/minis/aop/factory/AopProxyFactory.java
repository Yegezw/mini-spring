package com.minis.aop.factory;

import com.minis.aop.advisor.Advisor;
import com.minis.aop.proxy.AopProxy;

/**
 * AOP 代理工厂
 */
public interface AopProxyFactory {

    /**
     * 创建 AOP 代理
     * @param target 目标对象
     * @param advisor 增强器
     * @return AOP 代理
     */
    AopProxy createAopProxy(Object target, Advisor advisor);
}
