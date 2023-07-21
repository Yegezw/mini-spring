package com.minis.beans.factory.support;

import com.minis.beans.factory.config.BeanDefinition;

/**
 * Bean 定义注册表(BeanDefinition 仓库)
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    void removeBeanDefinition(String beanName);

    BeanDefinition getBeanDefinition(String beanName);

    boolean containsBeanDefinition(String beanName);
}
