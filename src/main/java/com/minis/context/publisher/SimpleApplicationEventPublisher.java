package com.minis.context.publisher;

import com.minis.context.listener.ApplicationListener;
import com.minis.context.event.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单应用事件发布者
 */
public class SimpleApplicationEventPublisher implements ApplicationEventPublisher {

    List<ApplicationListener> listeners = new ArrayList<>();

    @Override
    public void publishEvent(ApplicationEvent event) {
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        listeners.add(listener);
    }
}
