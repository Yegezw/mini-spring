package com.minis.beans.factory.postprocessor.factory;

import com.minis.beans.config.BeansException;
import com.minis.beans.factory.BeanFactory;

/**
 * Bean 工厂后置通知处理器
 */
public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
