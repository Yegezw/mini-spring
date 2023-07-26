package com.test.config.ioc;

import com.minis.context.listener.ApplicationListener;
import com.minis.context.event.ContextRefreshedEvent;

/**
 * 我的监听器
 */
public class MyListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println(".........refreshed.........beans count : " + event.getApplicationContext().getBeanDefinitionCount());
    }
}