package com.minis.beans.factory.registry.bean;

/**
 * 单例 Bean 注册表(单例 Bean 仓库)
 */
public interface SingletonBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();
}
