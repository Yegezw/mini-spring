package com.minis.aop.factory;

import com.minis.aop.proxy.AopProxy;
import com.minis.aop.proxy.JdkDynamicAopProxy;

/**
 * 默认 AOP 代理工厂
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target) {
        // Class<?> targetClass = target.getClass();
        // if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
            return new JdkDynamicAopProxy(target);
        // }
        // return new CglibAopProxy(config);
    }
}
