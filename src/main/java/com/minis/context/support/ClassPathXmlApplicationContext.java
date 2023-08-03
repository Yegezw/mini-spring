package com.minis.context.support;

import com.minis.beans.config.BeansException;
import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.XmlBeanDefinitionReader;
import com.minis.beans.factory.postprocessor.bean.BeanPostProcessor;
import com.minis.beans.factory.postprocessor.factory.BeanFactoryPostProcessor;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.event.ApplicationEvent;
import com.minis.context.event.ContextRefreshedEvent;
import com.minis.context.listener.ApplicationListener;
import com.minis.context.publisher.ApplicationEventPublisher;
import com.minis.context.publisher.SimpleApplicationEventPublisher;
import com.minis.core.resource.ClassPathXmlResource;
import com.minis.core.resource.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 类路径 XML 应用上下文
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    /**
     * <p>根据 fileName 创建 Resource
     * <p>创建 DefaultListableBeanFactory
     * <p>利用 XmlBeanDefinitionReader, 从 Resource 中解析 BeanDefinition 放入 DefaultListableBeanFactory
     * <p>调用 refresh()
     */
    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);

        beanFactory = new DefaultListableBeanFactory();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);

        if (isRefresh) {
            try {
                super.refresh();
            } catch (IllegalStateException | BeansException e) {
                e.printStackTrace();
            }
        }
    }

    // ======================================== AbstractApplicationContext ======================================== //

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String className = beanDefinition.className;
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (BeanFactoryPostProcessor.class.isAssignableFrom(clazz)) {
                try {
                    // BeanFactoryPostProcessor 完成实例化并注入
                    beanFactoryPostProcessors.add((BeanFactoryPostProcessor) beanFactory.getBean(beanDefinitionName));
                } catch (BeansException e) {
                    e.printStackTrace();
                }
            }
        }

        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessors) {
            try {
                beanFactoryPostProcessor.postProcessBeanFactory(bf);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }

        System.out.println("BeanFactoryPostProcessor 完成实例化并注入");
    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        String[] beanDefinitionNames = this.beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String className = beanDefinition.className;
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                try {
                    // BeanPostProcessor 完成实例化并注入
                    beanFactory.addBeanPostProcessor((BeanPostProcessor) beanFactory.getBean(beanDefinitionName));
                } catch (BeansException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("BeanPostProcessor 完成实例化并注入");
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        super.setApplicationEventPublisher(aep);
    }

    @Override
    public void onRefresh() {
        beanFactory.refresh();
    }

    @Override
    public void registerListeners() {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = null;
            try {
                bean = getBean(beanDefinitionName);
            } catch (BeansException e) {
                e.printStackTrace();
            }

            if (bean instanceof ApplicationListener) {
                super.getApplicationEventPublisher().addApplicationListener((ApplicationListener<?>) bean);
            }
        }

        System.out.println("ApplicationListener 注入完成");
    }

    @Override
    public void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    // ========================================= ApplicationEventPublisher ========================================= //

    @Override
    public void publishEvent(ApplicationEvent event) {
        super.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        super.getApplicationEventPublisher().addApplicationListener(listener);
    }
}
