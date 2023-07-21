package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;

/**
 * Bean 后置通知处理器
 */
public interface BeanPostProcessor {

    /**
     * 设置 Bean 工厂
     */
    void setBeanFactory(BeanFactory beanFactory);

    /**
     * Bean 初始化之前
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * Bean 初始化之后
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}