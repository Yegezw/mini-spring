package com.minis.beans.factory;

/**
 * BeanFactory 感知
 */
public interface BeanFactoryAware {

    /**
     * 设置 Bean 工厂
     */
    void setBeanFactory(BeanFactory beanFactory);
}
