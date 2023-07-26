package com.minis.context.listener;

import com.minis.context.event.ApplicationEvent;

import java.util.EventListener;

/**
 * 观察者: 事件监听器
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E event);
}
