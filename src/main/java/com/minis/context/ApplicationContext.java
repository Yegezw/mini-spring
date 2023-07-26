package com.minis.context;

import com.minis.beans.config.BeansException;
import com.minis.beans.factory.ListableBeanFactory;
import com.minis.beans.factory.postprocessor.factory.BeanFactoryPostProcessor;
import com.minis.beans.factory.ConfigurableBeanFactory;
import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.context.publisher.ApplicationEventPublisher;
import com.minis.core.env.Environment;
import com.minis.core.env.EnvironmentCapable;

/**
 * 应用上下文
 */
public interface ApplicationContext
        extends EnvironmentCapable, ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher {

    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void setEnvironment(Environment environment);

    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();
}
