package com.minis.web.config;

import com.minis.web.context.WebApplicationContext;
import com.minis.web.context.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ServletContext 监听器
 */
public class ContextLoaderListener implements ServletContextListener {

    /**
     * IOC 配置文件路径, web.xml 中配置, 存储在 ServletContext 中, 键为 contextConfigLocation
     */
    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
    /**
     * 父 XmlWeb 应用上下文, 由 Listener 负责启动, 用于 IOC 容器
     */
    private WebApplicationContext context;

    public ContextLoaderListener() {
    }

    public ContextLoaderListener(WebApplicationContext context) {
        this.context = context;
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        initWebApplicationContext(event.getServletContext());
    }

    /**
     * <p>创建 WebApplicationContext, 配置文件为 applicationContext.xml(在 web.xml 中配置)
     * <p>XmlWebApplicationContext 里有 ServletContext
     * <p>ServletContext 里有 XmlWebApplicationContext
     */
    private void initWebApplicationContext(ServletContext servletContext) {
        String applicationContext = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        System.out.println("父容器 XmlWebApplicationContext 配置文件: " + applicationContext); // applicationContext.xml

        WebApplicationContext wac = new XmlWebApplicationContext(applicationContext);
        wac.setServletContext(servletContext);

        context = wac;
        // 把父容器 XmlWebApplicationContext, 添加到 ServletContext 中
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);

        System.out.println("ContextLoaderListener 执行完毕");
    }
}
