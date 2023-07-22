package com.minis.web.config;

import com.minis.web.context.AnnotationConfigWebApplicationContext;
import com.minis.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ServletContext 监听器
 */
public class ContextLoaderListener implements ServletContextListener {

    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
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
     * <p>WebApplicationContext 里有 ServletContext
     * <p>ServletContext 里有 WebApplicationContext
     */
    private void initWebApplicationContext(ServletContext servletContext) {
        String applicationContext = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        System.out.println("WebApplicationContext: " + applicationContext); // applicationContext.xml

        WebApplicationContext wac = new AnnotationConfigWebApplicationContext(applicationContext);
        wac.setServletContext(servletContext);

        context = wac;
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);

        System.out.println("ContextLoaderListener 执行完毕");
    }
}
