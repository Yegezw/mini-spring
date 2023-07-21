package com.minis.context;

import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

/**
 * 类路径 XML 应用上下文
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    DefaultListableBeanFactory beanFactory;

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
    void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
    }

    @Override
    void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        super.setApplicationEventPublisher(aep);
    }

    @Override
    void onRefresh() {
        beanFactory.refresh();
    }

    @Override
    void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        super.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed ..."));
    }

    // ========================================= ApplicationEventPublisher ========================================= //

    @Override
    public void publishEvent(ApplicationEvent event) {
        super.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        super.getApplicationEventPublisher().addApplicationListener(listener);
    }
}
