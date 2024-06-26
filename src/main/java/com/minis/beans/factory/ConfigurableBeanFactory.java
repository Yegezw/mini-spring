package com.minis.beans.factory;

import com.minis.beans.factory.postprocessor.bean.BeanPostProcessor;
import com.minis.beans.factory.registry.bean.SingletonBeanRegistry;

/**
 * 可配置的 Bean 工厂
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();

    void registerDependentBean(String beanName, String dependentBeanName);

    String[] getDependentBeans(String beanName);

    String[] getDependenciesForBean(String beanName);
}
