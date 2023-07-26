package com.minis.web.servlet.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射仓库
 */
public class MappingRegistry {

    /**
     * &#064;RequestMapping 名称列表(URL)
     */
    public List<String> urlMappingNames = new ArrayList<>();
    /**
     * URL 名称与 Controller 对象的映射关系
     */
    public Map<String, Object> mappingObjs = new HashMap<>();
    /**
     * URL 名称与 Controller.method() 方法的映射关系
     */
    public Map<String, Method> mappingMethods = new HashMap<>();
    /**
     * URL 名称与类 Controller.method() 方法名的映射关系
     */
    public Map<String, String> mappingMethodNames = new HashMap<>();
    /**
     * URL 名称与 Controller 类的映射关系
     */
    public Map<String, Class<?>> mappingClasses = new HashMap<>();
}
