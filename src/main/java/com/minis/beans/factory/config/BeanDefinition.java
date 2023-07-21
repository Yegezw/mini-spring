package com.minis.beans.factory.config;

import com.minis.beans.PropertyValues;

import java.util.Objects;

/**
 * Bean 定义
 */
public class BeanDefinition {

    private static final String SCOPE_SINGLETON = "singleton";
    private static final String SCOPE_PROTOTYPE = "prototype";

    public String id;
    public String className;
    public volatile Object beanClass;
    public String scope = SCOPE_SINGLETON;

    public boolean lazyInit = false;                            // 懒加载
    public ConstructorArgumentValues constructorArgumentValues; // 构造器参数
    public PropertyValues propertyValues;                       // setter 参数
    public String[] dependsOn;                                  // 依赖的其它 Bean 的 BeanId(BeanName) 数组
    public String initMethodName;                               // 初始化方法名

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    /**
     * 是否单例
     */
    public boolean isSingleton() {
        return Objects.equals(scope, SCOPE_SINGLETON);
    }

    /**
     * 是否多例
     */
    public boolean isPrototype() {
        return Objects.equals(scope, SCOPE_PROTOTYPE);
    }
}
