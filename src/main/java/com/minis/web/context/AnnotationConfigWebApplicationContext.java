package com.minis.web.context;


import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.*;
import com.minis.web.config.XmlScanComponentHelper;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 子 AnnotationConfigWeb 应用上下文, 由 DispatcherServlet 负责启动, 用于 Web 容器
 */
public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {

    /**
     * 父 XmlWeb 应用上下文, 由 Listener 负责启动, 用于 IOC 容器
     */
    private WebApplicationContext parentApplicationContext;
    private ServletContext servletContext;
    private DefaultListableBeanFactory beanFactory;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public AnnotationConfigWebApplicationContext(String fileName) {
        this(fileName, null); // 这里传递 null 不合适
    }

    public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        this.servletContext = parentApplicationContext.getServletContext();

        // 读取配置文件, 获得 Controller 所在包, 进而获得 Controller 名称列表(类路径名) controllerNames
        URL xmlPath = null;
        try {
            xmlPath = servletContext.getResource(fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath); // 需要扫描的 package 列表
        List<String> controllerNames = scanPackages(packageNames);                // Controller 名称列表(类路径名)

        beanFactory = new DefaultListableBeanFactory();
        beanFactory.setParent(parentApplicationContext.getBeanFactory());
        loadBeanDefinitions(controllerNames);

        if (true) {
            try {
                // Controller 的实例化在这里完成
                // getBean() 进行实例化过程中, 会对 @Autowired 进行解析, 并注入需要的 bean
                super.refresh();
            } catch (IllegalStateException | BeansException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>创建所有 Controller 的 BeanDefinition, 供 getBean() 使用
     * <p>注意: 完成之后 beanFactory.getBeanDefinitionNames() == controllerNames(类路径名)
     */
    public void loadBeanDefinitions(List<String> controllerNames) {
        for (String controllerName : controllerNames) {
            // beanId = beanClassName = controllerName
            BeanDefinition beanDefinition = new BeanDefinition(controllerName, controllerName);
            beanFactory.registerBeanDefinition(controllerName, beanDefinition);
        }
    }

    private List<String> scanPackages(List<String> packages) {
        List<String> controllerNames = new ArrayList<>();

        for (String packageName : packages) {
            controllerNames.addAll(scanPackage(packageName));
        }

        return controllerNames;
    }

    @SuppressWarnings("all")
    private List<String> scanPackage(String packageName) {
        List<String> controllerNames = new ArrayList<>();

        // 将以 . 分割的包名, 换成以 / 分割的 URL
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) scanPackage(packageName + "." + file.getName());
            else {
                String controllerName = packageName + "." + file.getName().replace(".class", "");
                controllerNames.add(controllerName);
            }
        }

        return controllerNames;
    }

    public void setParent(WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        beanFactory.setParent(parentApplicationContext.getBeanFactory());
    }

    // ======================================== WebApplicationContext ======================================== //

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    // ======================================= AbstractApplicationContext ======================================= //

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        super.setApplicationEventPublisher(aep);
    }

    @Override
    public void onRefresh() {
        beanFactory.refresh();
    }

    @Override
    public void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        super.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void finishRefresh() {
        // TODO Auto-generated method stub
    }

    // ======================================== ApplicationEventPublisher ======================================== //

    @Override
    public void publishEvent(ApplicationEvent event) {
        super.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        super.getApplicationEventPublisher().addApplicationListener(listener);
    }
}
