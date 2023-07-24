package com.minis.context;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象应用上下文
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private Environment environment;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
    private long startupDate;
    private final AtomicBoolean active = new AtomicBoolean(); // 活跃的
    private final AtomicBoolean closed = new AtomicBoolean(); // 关闭的
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 刷新
     */
    @Override
    public void refresh() throws BeansException, IllegalStateException {
        System.out.println("AbstractApplicationContext.refresh() 开始执行");

        postProcessBeanFactory(getBeanFactory());

        registerBeanPostProcessors(getBeanFactory());

        initApplicationEventPublisher();

        onRefresh();

        registerListeners();

        finishRefresh();

        System.out.println("AbstractApplicationContext.refresh() 执行完毕\n");
    }

    /**
     * 后置通知 BeanFactory
     */
    public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);

    /**
     * 注册 BeanPostProcessor
     */
    public abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory);

    /**
     * 初始化应用事件发布者(被观察者)
     */
    public abstract void initApplicationEventPublisher();

    /**
     * 注册监听器(观察者)
     */
    public abstract void registerListeners();

    /**
     * 在刷新
     */
    public abstract void onRefresh();

    /**
     * 结束刷新
     */
    public abstract void finishRefresh();

    // ========================================= ApplicationContext ========================================= //

    @Override
    public String getApplicationName() {
        return "";
    }

    @Override
    public long getStartupDate() {
        return startupDate;
    }

    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        beanFactoryPostProcessors.add(postProcessor);
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    // ========================================= BeanFactory ========================================= //

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object bean = getBeanFactory().getBean(beanName);
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(this);
        }
        return bean;
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    // public void registerBean(String beanName, Object obj) {
    //     getBeanFactory().registerBean(beanName, obj);
    // }

    @Override
    public boolean isSingleton(String name) {
        return getBeanFactory().isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) {
        return getBeanFactory().isPrototype(name);
    }

    @Override
    public Class<?> getType(String name) {
        return getBeanFactory().getType(name);
    }

    // ========================================= ListableBeanFactory ========================================= //

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    // ========================================= SingletonBeanRegistry ========================================= //

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        getBeanFactory().registerSingleton(beanName, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) {
        return getBeanFactory().getSingleton(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return getBeanFactory().containsSingleton(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return getBeanFactory().getSingletonNames();
    }

    // ========================================= ConfigurableBeanFactory ========================================= //

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        getBeanFactory().addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return getBeanFactory().getBeanPostProcessorCount();
    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {
        getBeanFactory().registerDependentBean(beanName, dependentBeanName);
    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return getBeanFactory().getDependentBeans(beanName);
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return getBeanFactory().getDependenciesForBean(beanName);
    }

    // ========================================= setter、getter ========================================= //

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return beanFactoryPostProcessors;
    }
}
