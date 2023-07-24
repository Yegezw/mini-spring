package com.minis.context;

import com.minis.beans.BeansException;

/**
 * 应用上下文感知
 */
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
