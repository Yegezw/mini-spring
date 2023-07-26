package com.minis.beans.factory.postprocessor.bean;

import com.minis.beans.config.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.annotation.Autowired;

import java.lang.reflect.Field;

/**
 * 自动装配注解 Bean 后置通知处理器
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            boolean isAutowired = field.isAnnotationPresent(Autowired.class);
            if (isAutowired) {
                String fieldName = field.getName();
                Object autowiredObj = beanFactory.getBean(fieldName); // fieldName 需要和依赖的 beanName 一致
                try {
                    field.setAccessible(true);     // 暴力反射
                    field.set(bean, autowiredObj); // 注入属性
                    System.out.println("autowire " + fieldName + " for bean " + beanName);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
