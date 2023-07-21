package com.minis.web;

import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import com.minis.web.config.MappingValue;
import com.minis.web.config.XmlConfigReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件: web.xml
 */
public class DispatcherServlet extends HttpServlet {

    private Map<String, MappingValue> mappingValues;
    private Map<String, Class<?>> mappingClazz = new HashMap<>();
    private Map<String, Object> mappingObjs = new HashMap<>();

    public DispatcherServlet() {
        System.out.println("DispatcherServlet 实例化完成");
    }

    /**
     * <p>根据 ServletConfig 获取 contextConfigLocation 路径(/WEB-INF/minisMVC-servlet.xml)
     * <p>根据路径创建 Resource
     * <p>利用 XmlConfigReader, 从 Resource 中解析 MappingValue 放入 mappingValues
     * <p>调用 refresh()
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath;
        try {
            xmlPath = super.getServletContext().getResource(contextConfigLocation);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Resource resource = new ClassPathXmlResource(xmlPath);
        XmlConfigReader reader = new XmlConfigReader();
        mappingValues = reader.loadConfig(resource);

        refresh();
        System.out.println("DispatcherServlet 初始化完成");
    }

    /**
     * 读取 mappingValues 中的 Bean 定义, 加载类, 创建实例
     */
    protected void refresh() {
        for (Map.Entry<String, MappingValue> entry : mappingValues.entrySet()) {
            String id = entry.getKey();
            String className = entry.getValue().clazz;
            Class<?> clazz = null;
            Object obj = null;
            try {
                clazz = Class.forName(className);
                obj = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mappingClazz.put(id, clazz);
            mappingObjs.put(id, obj);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if (mappingValues.get(path) == null) return;

        Class<?> clazz = mappingClazz.get(path);
        Object obj = mappingObjs.get(path);
        String methodName = mappingValues.get(path).method;
        Object result = null;
        try {
            Method method = clazz.getMethod(methodName);
            result = method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result != null) resp.getWriter().append(result.toString());
    }
}
