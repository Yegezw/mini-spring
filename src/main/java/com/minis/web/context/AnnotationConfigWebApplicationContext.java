package com.minis.web.context;


import com.minis.beans.config.BeansException;
import com.minis.beans.factory.postprocessor.bean.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.postprocessor.factory.BeanFactoryPostProcessor;
import com.minis.beans.factory.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.event.ApplicationEvent;
import com.minis.context.event.ContextRefreshedEvent;
import com.minis.context.listener.ApplicationListener;
import com.minis.context.publisher.ApplicationEventPublisher;
import com.minis.context.publisher.SimpleApplicationEventPublisher;
import com.minis.context.support.AbstractApplicationContext;
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

        System.out.println(
                "1、设置 parentApplicationContext 和 servletContext\n" +
                        "1、读取配置文件 -> 解析 Controller 类路径列表\n" +
                        "1、创建 Controller 的 BeanDefinition, 注册给 BeanFactory"
        );

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
        System.out.println("2、postProcessBeanFactory(bf) 执行完成");
    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory(this);
        beanFactory.addBeanPostProcessor(autowiredAnnotationBeanPostProcessor);

        System.out.println("3、registerBeanPostProcessors(bf) 执行完成");
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        super.setApplicationEventPublisher(aep);

        System.out.println("4、初始化应用事件发布者（被观察者）完成");
    }

    @Override
    public void onRefresh() {
        beanFactory.refresh();

        System.out.println("5、刷新完成");
    }

    @Override
    public void registerListeners() {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = null;
            try {
                bean = getBean(beanDefinitionName);
            } catch (BeansException e) {
                e.printStackTrace();
            }

            if (bean instanceof ApplicationListener) {
                super.getApplicationEventPublisher().addApplicationListener((ApplicationListener<?>) bean);
            }
        }

        System.out.println("6、ApplicationListener 注入完成");
    }

    @Override
    public void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    // ======================================== ApplicationEventPublisher ======================================== //

    @Override
    public void publishEvent(ApplicationEvent event) {
        super.getApplicationEventPublisher().publishEvent(event);

        System.out.println("7、结束刷新");
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        super.getApplicationEventPublisher().addApplicationListener(listener);
    }
}
