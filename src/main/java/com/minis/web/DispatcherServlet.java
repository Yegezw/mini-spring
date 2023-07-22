package com.minis.web;

import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.config.RequestMapping;
import com.minis.web.config.XmlScanComponentHelper;
import com.minis.web.context.WebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置文件: web.xml
 */
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    /**
     * Web 应用上下文
     */
    private WebApplicationContext webApplicationContext;

    /**
     * Controller 配置文件路径
     */
    private String contextConfigLocation;
    /**
     * 需要扫描的 package 列表
     */
    private List<String> packageNames = new ArrayList<>();


    /**
     * Controller 名称列表(类路径名)
     */
    private List<String> controllerNames = new ArrayList<>();
    /**
     * Controller 名称与对象的映射关系
     */
    private Map<String, Object> controllerObjs = new HashMap<>();
    /**
     * Controller 名称与类的映射关系
     */
    private Map<String, Class<?>> controllerClazzes = new HashMap<>();

    /**
     * &#064;RequestMapping 名称列表(URL)
     */
    private List<String> urlMappingNames = new ArrayList<>();
    /**
     * URL 名称与对象(Controller)的映射关系
     */
    private Map<String, Object> mappingObjs = new HashMap<>();
    /**
     * URL 名称与类(Controller)的映射关系
     */
    private Map<String, Method> mappingMethods = new HashMap<>();

    public DispatcherServlet() {
        System.out.println("DispatcherServlet 实例化完成");
    }

    /**
     * <p>从 ServletContext 中获取: Listener 启动时注册好的 WebApplicationContext
     * <p>通过 ServletConfig 获取 contextConfigLocation 路径(/WEB-INF/minisMVC-servlet.xml)
     * <p>读取配置文件, 获得 Controller 所在包
     * <p>调用 refresh()
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        webApplicationContext = (WebApplicationContext) super.getServletContext().
                getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        contextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath;
        try {
            xmlPath = super.getServletContext().getResource(contextConfigLocation);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

        refresh();
        System.out.println("DispatcherServlet 初始化完成");
    }

    /**
     * <p>加载所有 Controller 类, 并进行实例化
     * <p>扫描所有 Controller 类中, 被 @RequestMapping 标注的方法, 并进行映射
     * <p>URL -> Controller + Method
     */
    protected void refresh() {
        initController();
        initMapping();
    }

    /**
     * 初始化 Controller: 加载所有 Controller 类, 并进行实例化
     */
    protected void initController() {
        controllerNames = scanPackages(packageNames);

        for (String controllerName : controllerNames) {
            Class<?> clazz = null;
            Object obj;

            try {
                clazz = Class.forName(controllerName);
                controllerClazzes.put(controllerName, clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                obj = clazz.newInstance();
                populateBean(obj);
                controllerObjs.put(controllerName, obj);
            } catch (InstantiationException | IllegalAccessException | BeansException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>初始化 URL 映射: 扫描所有 Controller 类中, 被 &#064;RequestMapping 标注的方法, 并进行映射
     * <p>URL -> Controller + Method
     */
    protected void initMapping() {
        for (String controllerName : controllerNames) {
            Class<?> clazz = controllerClazzes.get(controllerName);
            Object obj = controllerObjs.get(controllerName);

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                if (isRequestMapping) {
                    String urlMapping = method.getAnnotation(RequestMapping.class).value();
                    urlMappingNames.add(urlMapping);
                    mappingObjs.put(urlMapping, obj);
                    mappingMethods.put(urlMapping, method);
                }
            }
        }
    }

    /**
     * 填充 Bean
     */
    protected void populateBean(Object bean) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            boolean isAutowired = field.isAnnotationPresent(Autowired.class);
            if (isAutowired) {
                String fieldName = field.getName();
                Object autowiredObj = webApplicationContext.getBean(fieldName); // fieldName 需要和依赖的 beanName 一致
                try {
                    field.setAccessible(true);     // 暴力反射
                    field.set(bean, autowiredObj); // 注入属性
                    System.out.println("autowire " + fieldName + " for bean " + clazz.getSimpleName());
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        // System.out.println(path);
        if (!urlMappingNames.contains(path)) return;

        Object obj = mappingObjs.get(path);
        Method method = mappingMethods.get(path);
        Object result = null;
        try {
            result = method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result != null) resp.getWriter().append(result.toString());
    }
}
