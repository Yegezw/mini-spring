package com.minis.context;

/**
 * 上下文刷新事件
 */
public class ContextRefreshEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public ContextRefreshEvent(Object source) {
        super(source);
    }

    public String toString() {
        return super.message;
    }
}
