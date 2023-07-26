package com.test.config.ioc;

import com.minis.beans.config.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.postprocessor.factory.BeanFactoryPostProcessor;

/**
 * My Bean 工厂后置通知处理器
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println(".........MyBeanFactoryPostProcessor...........");
	}
}