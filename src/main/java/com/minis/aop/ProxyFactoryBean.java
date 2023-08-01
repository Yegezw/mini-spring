package com.minis.aop;

import com.minis.aop.factory.AopProxyFactory;
import com.minis.aop.factory.DefaultAopProxyFactory;
import com.minis.aop.proxy.AopProxy;
import com.minis.beans.factory.FactoryBean;
import com.minis.web.servlet.adapter.util.ClassUtils;

/**
 * 代理 FactoryBean
 */
public class ProxyFactoryBean implements FactoryBean<Object> {

    /**
     * AOP 代理工厂
     */
    private AopProxyFactory aopProxyFactory;
    private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();

    /**
     * 目标对象 BeanName
     */
    private String targetName;
    /**
     * 目标对象
     */
    private Object target;
    /**
     * 代理对象
     */
    private Object singletonInstance;
    /**
     * 拦截名称
     */
    private String[] interceptorNames;

    public ProxyFactoryBean() {
        aopProxyFactory = new DefaultAopProxyFactory();
    }

    @Override
    public Object getObject() throws Exception {
        return getSingletonInstance();
    }

    /**
     * 获取代理对象
     */
    private synchronized Object getSingletonInstance() {
        // 如果没有创建过代理对象, 就进行创建
        if (singletonInstance == null) singletonInstance = getProxy(createAopProxy());
        System.out.println("---------- In ProxyFactoryBean.getSingletonInstance(), return proxy for: " + singletonInstance + ", " + singletonInstance.getClass());
        return singletonInstance;
    }

    /**
     * 生成代理对象
     */
    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    /**
     * 创建 AOP 代理(JDK / Cglib 动态代理)
     */
    protected AopProxy createAopProxy() {
        System.out.println("---------- In ProxyFactoryBean.createAopProxy(), createAopProxy for: " + target);
        return getAopProxyFactory().createAopProxy(target);
    }

    @Override
    public Class<?> getObjectType() {
        // TODO Auto-generated method stub
        return null;
    }

    // ======================================== getter()、setter() ========================================

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }

    public AopProxyFactory getAopProxyFactory() {
        return aopProxyFactory;
    }

    public void setInterceptorNames(String... interceptorNames) {
        this.interceptorNames = interceptorNames;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }
}
