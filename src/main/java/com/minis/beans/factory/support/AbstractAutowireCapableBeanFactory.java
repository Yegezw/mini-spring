package com.minis.beans.factory.support;

import com.minis.beans.config.BeansException;
import com.minis.beans.factory.AutowireCapableBeanFactory;
import com.minis.beans.factory.postprocessor.bean.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象自动装配的 Bean 工厂
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : beanPostProcessors) {
            beanProcessor.setBeanFactory(this);
            result = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if(result == null) return null; // 这里应该抛出异常更好
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : beanPostProcessors) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if(result == null) return null; // 这里应该抛出异常更好
        }
        return result;
    }
}
