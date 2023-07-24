package com.minis.web.servlet.mapping;

import com.minis.beans.BeansException;
import com.minis.context.ApplicationContext;
import com.minis.context.ApplicationContextAware;
import com.minis.web.config.RequestMapping;
import com.minis.web.servlet.config.HandlerMethod;
import com.minis.web.servlet.config.MappingRegistry;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 请求映射(处理器映射器)
 */
public class RequestMappingHandlerMapping implements HandlerMapping, ApplicationContextAware {

    /**
     * 子 AnnotationConfigWeb 应用上下文, 由 DispatcherServlet 负责启动
     */
    private ApplicationContext applicationContext = null;
    /**
     * 映射仓库
     */
    private MappingRegistry mappingRegistry = null;

    public RequestMappingHandlerMapping() {
    }

    /**
     * <p>初始化 URL 映射: 扫描所有 Controller 类中, 被 @RequestMapping 标注的方法, 并进行映射
     * <p>URL -> Controller + Method
     */
    protected void initMapping() {
        String[] controllerNames = applicationContext.getBeanDefinitionNames(); // Controller 名称列表(类路径名)
        for (String controllerName : controllerNames) {
            Class<?> clazz;
            Object obj;
            try {
                clazz = Class.forName(controllerName);
                obj = applicationContext.getBean(controllerName);
            } catch (ClassNotFoundException | BeansException e) {
                throw new RuntimeException(e);
            }

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                if (isRequestMapping) {
                    String urlMapping = method.getAnnotation(RequestMapping.class).value();
                    mappingRegistry.urlMappingNames.add(urlMapping);
                    mappingRegistry.mappingObjs.put(urlMapping, obj);
                    mappingRegistry.mappingMethods.put(urlMapping, method);
                }
            }
        }
    }

    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        if (mappingRegistry == null) {
            mappingRegistry = new MappingRegistry();
            initMapping();
        }

        String path = request.getServletPath();
        // System.out.println(path);
        if (!mappingRegistry.urlMappingNames.contains(path)) return null;

        Object obj = mappingRegistry.mappingObjs.get(path);
        Method method = mappingRegistry.mappingMethods.get(path);
        return new HandlerMethod(obj, method);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
