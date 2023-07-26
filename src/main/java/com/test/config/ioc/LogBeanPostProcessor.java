package com.test.config.ioc;

import com.minis.beans.config.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.postprocessor.bean.BeanPostProcessor;

/**
 * Log Bean 后置通知处理器
 */
public class LogBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization : " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization : " + beanName);
        return bean;
    }
}