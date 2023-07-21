package com.minis.beans.factory.support;

import com.minis.beans.factory.config.SingletonBeanRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认单例 Bean 注册表
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected List<String> beanNames = new ArrayList<>();
    protected Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * beanName : Set<dependentBeanName>
     */
    protected Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
    /**
     * dependentBeanName : Set<beanName>
     */
    protected Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (singletonObjects) {
            Object oldObject = singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + singletonObject +
                        "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }

            singletonObjects.put(beanName, singletonObject);
            beanNames.add(beanName);
            System.out.println("bean registered: " + beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return beanNames.toArray(new String[0]);
    }

    public void removeSingleton(String beanName) {
        synchronized (singletonObjects) {
            singletonObjects.remove(beanName);
            beanNames.remove(beanName);
        }
    }

    /**
     * 注册依赖 Bean
     */
    public void registerDependentBean(String beanName, String dependentBeanName) {
        Set<String> dependentBeans = dependentBeanMap.get(beanName);
        if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) return;

        // No entry yet -> fully synchronized manipulation of the dependentBeans Set
        synchronized (dependentBeanMap) {
            dependentBeans = dependentBeanMap.get(beanName);
            if (dependentBeans == null) {
                dependentBeans = new LinkedHashSet<>(8);
                dependentBeanMap.put(beanName, dependentBeans);
            }
            dependentBeans.add(dependentBeanName);
        }
        synchronized (dependenciesForBeanMap) {
            Set<String> dependenciesForBean = dependenciesForBeanMap.get(dependentBeanName);
            if (dependenciesForBean == null) {
                dependenciesForBean = new LinkedHashSet<>(8);
                dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
            }
            dependenciesForBean.add(beanName);
        }
    }

    /**
     * beanName 是否依赖其它 Bean
     */
    public boolean hasDependentBean(String beanName) {
        return dependentBeanMap.containsKey(beanName);
    }

    /**
     * 获取 beanName 依赖其它 Bean 的 String[beanName]
     */
    public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = dependentBeanMap.get(beanName);
        if (dependentBeans == null) return new String[0];
        return dependentBeans.toArray(new String[0]);
    }

    /**
     * 获取其它 Bean 依赖 beanName 的 String[beanName]
     */
    public String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) return new String[0];
        return dependenciesForBean.toArray(new String[0]);
    }
}
