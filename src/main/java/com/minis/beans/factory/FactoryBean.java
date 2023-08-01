package com.minis.beans.factory;

/**
 * 工厂 Bean
 */
public interface FactoryBean<T> {

    /**
     * 获取内部对象
     */
    T getObject() throws Exception;

    /**
     * 获取内部对象类型
     */
    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }
}
