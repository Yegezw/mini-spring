package com.minis.aop.autoproxy;

import com.minis.aop.ProxyFactoryBean;
import com.minis.aop.advisor.PointcutAdvisor;
import com.minis.aop.factory.AopProxyFactory;
import com.minis.aop.factory.DefaultAopProxyFactory;
import com.minis.aop.proxy.AopProxy;
import com.minis.beans.config.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.postprocessor.bean.BeanPostProcessor;
import com.minis.util.PatternMatchUtils;

/**
 * Bean 名称自动代理创建者
 */
public class BeanNameAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

    private String pattern;
    private BeanFactory beanFactory;
    /**
     * AOP 代理工厂
     */
    private AopProxyFactory aopProxyFactory;

    /**
     * 增强器名称
     */
    private String advisorName;
    /**
     * 切入点增强器
     */
    private PointcutAdvisor advisor;

    public BeanNameAutoProxyCreator() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    /**
     * 核心方法: 在 bean 实例化之后, init-method 调用之前执行这个步骤
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("try to create proxy for: " + beanName);

        if (isMatch(beanName, pattern)) {
            System.out.println(beanName + " is matched about " + pattern + ", create proxy for " + beanName);

            // 创建代理对象
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setBeanFactory(beanFactory);
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            proxyFactoryBean.setAdvisorName(advisorName); // 设置增强器名称

            return proxyFactoryBean;
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 创建 AOP 代理(JDK / Cglib 动态代理)
     */
    protected AopProxy createAopProxy(Object target) {
        return aopProxyFactory.createAopProxy(target, advisor);
    }

    /**
     * 生成代理对象
     */
    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    /**
     * 核心方法, 判断 BeanName 是否匹配给定的模式
     */
    protected boolean isMatch(String beanName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }

    // ======================================== getter()、setter() ========================================

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setAdvisor(PointcutAdvisor advisor) {
        this.advisor = advisor;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }
}
