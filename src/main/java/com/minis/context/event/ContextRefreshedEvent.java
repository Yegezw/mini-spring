package com.minis.context.event;

import com.minis.context.ApplicationContext;

/**
 * 刷新事件
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = 1L;

    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }

    public String toString() {
        return super.message;
    }
}
