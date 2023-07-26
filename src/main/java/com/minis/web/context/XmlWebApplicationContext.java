package com.minis.web.context;

import com.minis.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;

/**
 * 父 XmlWeb 应用上下文, 由 Listener 负责启动, 用于 IOC 容器
 */
public class XmlWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    public XmlWebApplicationContext(String fileName) {
        super(fileName);
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
