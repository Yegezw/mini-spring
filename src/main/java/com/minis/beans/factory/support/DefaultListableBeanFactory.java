package com.minis.beans.factory.support;

import com.minis.beans.config.BeansException;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * IOC 引擎
 */
public class DefaultListableBeanFactory
        extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {

    private ConfigurableListableBeanFactory parentBeanFactory;

    @Override
    public int getBeanDefinitionCount() {
        return super.beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return super.beanDefinitionNames.toArray(new String[0]);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> list = new ArrayList<>();

        for (String beanName : super.beanDefinitionNames) {
            BeanDefinition beanDefinition = super.getBeanDefinition(beanName);
            Class<?> clazz = beanDefinition.getClass();
            if (type.isAssignableFrom(clazz)) list.add(beanName);
        }

        return list.toArray(new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        String[] beanNames = getBeanNamesForType(type);

        Map<String, T> map = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            Object beanInstance = getBean(beanName);
            map.put(beanName, (T) beanInstance);
        }

        return map;
    }

    public void setParent(ConfigurableListableBeanFactory beanFactory) {
        this.parentBeanFactory = beanFactory;
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object result = super.getBean(beanName);
        if (result == null && parentBeanFactory != null) result = parentBeanFactory.getBean(beanName); // 避免空指针异常
        return result;
    }
}
