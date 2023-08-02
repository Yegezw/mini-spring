package com.minis.beans.factory;

/**
 * BeanFactory 感知
 */
public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory beanFactory);
}
