package com.minis.context.publisher;

import com.minis.context.listener.ApplicationListener;
import com.minis.context.event.ApplicationEvent;

/**
 * 被观察者: 应用事件发布者
 */
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);

    void addApplicationListener(ApplicationListener<?> listener);
}
