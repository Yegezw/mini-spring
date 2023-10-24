package com.minis.web.servlet;

import com.minis.beans.config.BeansException;
import com.minis.web.config.XmlScanComponentHelper;
import com.minis.web.context.AnnotationConfigWebApplicationContext;
import com.minis.web.context.WebApplicationContext;
import com.minis.web.servlet.adapter.HandlerAdapter;
import com.minis.web.servlet.config.HandlerMethod;
import com.minis.web.servlet.mapping.HandlerMapping;
import com.minis.web.servlet.resolver.ViewResolver;
import com.minis.web.servlet.resolver.view.ModelAndView;
import com.minis.web.servlet.resolver.view.View;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * 通过配置文件 web.xml 来创建 DispatcherServlet, 执行 init(ServletConfig)
 */
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";

    /**
     * 父 XmlWeb 应用上下文, 由 Listener 负责启动, 用于 IOC 容器
     */
    private WebApplicationContext parentApplicationContext;
    /**
     * 子 AnnotationConfigWeb 应用上下文, 由 DispatcherServlet 负责启动, 用于 Web 容器
     */
    private WebApplicationContext webApplicationContext;

    /**
     * Controller 配置文件路径, web.xml 中配置, 存储在 ServletConfig 中, 键为 contextConfigLocation
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
     * 处理器映射器: HttpServletRequest -> URL -> Controller + Method -> HandlerMethod
     */
    private HandlerMapping handlerMapping;
    /**
     * 处理器适配器
     */
    private HandlerAdapter handlerAdapter;
    /**
     * 视图解析器
     */
    private ViewResolver viewResolver;

    public DispatcherServlet() {
        System.out.println("------------------------- DispatcherServlet 实例化完成 -------------------------");
    }

    /**
     * <p>从 ServletContext 中获取 Listener 启动时注册好的 XmlWebApplicationContext 作为父容器
     * <p>通过 ServletConfig 获取 contextConfigLocation 路径(/WEB-INF/minisMVC-servlet.xml)
     * <p>读取配置文件, 获得 Controller 所在包
     * <p>调用 refresh()
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        parentApplicationContext = (WebApplicationContext) super.getServletContext().
                getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        contextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath;
        try {
            xmlPath = super.getServletContext().getResource(contextConfigLocation);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

        System.out.println("子容器 AnnotationConfigWebApplicationContext 配置文件: " + contextConfigLocation);
        // 在创建子容器时, 会创建 ControllerBeanDefinition
        // 创建所有 Controller 的 BeanDefinition, 供 getBean() 使用
        // 完成之后 webApplicationContext.getBeanDefinitionNames() == controllerNames(类路径名)
        // 然后进行 refresh(), Controller 的实例化在这里完成
        webApplicationContext = new AnnotationConfigWebApplicationContext(contextConfigLocation, parentApplicationContext);
        refresh();
        System.out.println("------------------------- DispatcherServlet 初始化完成 -------------------------");
    }

    /**
     * <p>初始化 Controller: controllerNames、controllerClazzes、controllerObjs
     * <p>初始化 -> 处理器映射器
     * <p>初始化 -> 处理器适配器
     * <p>初始化 -> 视图解析器
     */
    protected void refresh() {
        initController();

        initHandlerMappings(webApplicationContext); // 初始化 -> 处理器映射器
        initHandlerAdapters(webApplicationContext); // 初始化 -> 处理器适配器
        initViewResolvers(webApplicationContext);   // 初始化 -> 视图解析器
    }

    /**
     * 初始化 Controller: controllerNames、controllerClazzes、controllerObjs
     */
    protected void initController() {
        controllerNames = Arrays.asList(webApplicationContext.getBeanDefinitionNames());
        for (String controllerName : this.controllerNames) {
            try {
                controllerClazzes.put(controllerName, Class.forName(controllerName));
                controllerObjs.put(controllerName, webApplicationContext.getBean(controllerName));
                System.out.println("Controller: " + controllerName);
            } catch (ClassNotFoundException | BeansException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化 -> 处理器映射器
     */
    protected void initHandlerMappings(WebApplicationContext webApplicationContext) {
        try {
            handlerMapping = (HandlerMapping) webApplicationContext.getBean(HANDLER_MAPPING_BEAN_NAME);
        } catch (BeansException e) {
            System.out.println("handlerMapping 获取失败");
            e.printStackTrace();
        }
    }

    /**
     * 初始化 -> 处理器适配器
     */
    protected void initHandlerAdapters(WebApplicationContext webApplicationContext) {
        try {
            handlerAdapter = (HandlerAdapter) webApplicationContext.getBean(HANDLER_ADAPTER_BEAN_NAME);
        } catch (BeansException e) {
            System.out.println("handlerAdapter 获取失败");
            e.printStackTrace();
        }
    }

    /**
     * 初始化 -> 视图解析器
     */
    protected void initViewResolvers(WebApplicationContext webApplicationContext) {
        try {
            viewResolver = (ViewResolver) webApplicationContext.getBean(VIEW_RESOLVER_BEAN_NAME);
        } catch (BeansException e) {
            System.out.println("viewResolver 获取失败");
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        // 将子容器 AnnotationConfigWebApplicationContext 放入 request 中
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);

        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = handlerMapping.getHandler(request);
        if (handlerMethod == null) return;

        ModelAndView mv = handlerAdapter.handle(request, response, handlerMethod);
        render(request, response, mv);
    }

    /**
     * 渲染
     */
    protected void render(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        if (mv == null) {
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        View view;
        Map<String, Object> modelMap = mv.getModel();
        if (mv.isReference()) {
            String viewName = mv.getViewName();
            view = resolveViewName(viewName, modelMap, request);
        } else view = mv.getView();

        view.render(modelMap, request, response);
    }

    protected View resolveViewName(String viewName, Map<String, Object> model, HttpServletRequest request) throws Exception {
        if (viewResolver != null) return viewResolver.resolveViewName(viewName);
        return null;
    }
}
