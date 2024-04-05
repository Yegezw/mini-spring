package com.minis.beans.factory.postprocessor.bean;

import com.minis.beans.config.BeansException;

/**
 * Bean 后置通知处理器
 */
public interface BeanPostProcessor {

    /**
     * Bean 初始化之前
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * Bean 初始化之后
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}