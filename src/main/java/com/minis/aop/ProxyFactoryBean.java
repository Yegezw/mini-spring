package com.minis.aop;

import com.minis.aop.advisor.PointcutAdvisor;
import com.minis.aop.factory.AopProxyFactory;
import com.minis.aop.factory.DefaultAopProxyFactory;
import com.minis.aop.proxy.AopProxy;
import com.minis.beans.config.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.FactoryBean;
import com.minis.util.ClassUtils;

/**
 * 代理 FactoryBean
 */
public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {

    private BeanFactory beanFactory;

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
     * 增强器名称
     */
    private String advisorName;
    /**
     * 切入点增强器
     */
    private PointcutAdvisor advisor;

    public ProxyFactoryBean() {
        aopProxyFactory = new DefaultAopProxyFactory();
    }

    @Override
    public Object getObject() throws Exception {
        initializeAdvisor();
        return getSingletonInstance();
    }

    /**
     * 初始化增强器
     */
    private synchronized void initializeAdvisor() {
        Object advisor = null;

        try {
            advisor = beanFactory.getBean(advisorName);
        } catch (BeansException e) {
            e.printStackTrace();
        }

        // TODO 现在只支持 PointcutAdvisor
        if (advisor instanceof PointcutAdvisor) this.advisor = (PointcutAdvisor) advisor;
    }

    /**
     * 获取代理对象
     */
    private synchronized Object getSingletonInstance() {
        // 如果没有创建过代理对象, 就进行创建
        if (singletonInstance == null) singletonInstance = getProxy(createAopProxy());
        return singletonInstance;
    }

    /**
     * 创建 AOP 代理(JDK / Cglib 动态代理)
     */
    protected AopProxy createAopProxy() {
        return getAopProxyFactory().createAopProxy(target, advisor);
    }

    /**
     * 生成代理对象
     */
    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return target.getClass();
    }

    // ======================================== getter()、setter() ========================================

    public void setAdvisor(PointcutAdvisor advisor) {
        this.advisor = advisor;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }

    public AopProxyFactory getAopProxyFactory() {
        return aopProxyFactory;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
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
