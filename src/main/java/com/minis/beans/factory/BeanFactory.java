package com.minis.beans.factory;

import com.minis.beans.BeansException;

/**
 * Bean 工厂
 */
public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;

    boolean containsBean(String beanName);

    boolean isSingleton(String beanName);

    boolean isPrototype(String beanName);

    Class<?> getType(String beanName);
}
