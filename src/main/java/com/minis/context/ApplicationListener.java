package com.minis.context;

import java.util.EventListener;

/**
 * 事件监听器
 */
public class ApplicationListener implements EventListener {

    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println(event.toString());
    }
}
