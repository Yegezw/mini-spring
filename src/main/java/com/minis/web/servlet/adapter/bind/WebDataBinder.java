package com.minis.web.servlet.adapter.bind;

import com.minis.beans.config.PropertyValues;
import com.minis.web.servlet.adapter.convert.AbstractPropertyAccessor;
import com.minis.web.servlet.adapter.convert.BeanWrapperImpl;
import com.minis.web.servlet.adapter.convert.editor.PropertyEditor;
import com.minis.web.servlet.adapter.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * Web 数据绑定器
 */
public class WebDataBinder {

    /**
     * 方法参数对象
     */
    private Object target;
    /**
     * 方法参数类型 Class
     */
    private Class<?> clazz;
    /**
     * 方法参数对象名称
     */
    private String objectName;
    /**
     * 设置属性值的工具 BeanWrapperImpl
     */
    private AbstractPropertyAccessor propertyAccessor;

    public WebDataBinder(Object target) {
        this(target, "");
    }

    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.objectName = targetName;
        this.clazz = target.getClass();
        this.propertyAccessor = new BeanWrapperImpl(target);
    }

    /**
     * 核心绑定方法: 将 HttpServletRequest 里面的参数值绑定到目标对象的属性上
     */
    public void bind(HttpServletRequest request) {
        // TODO 简单类型
        // 复杂类型
        PropertyValues pvs = assignParameters(request);
        addBindValues(pvs, request);
        doBind(pvs);
    }

    /**
     * 分配参数: 把 HttpServletRequest 里与 target 对象相关的参数, 解析成 PropertyValues
     */
    private PropertyValues assignParameters(HttpServletRequest request) {
        // map 里包含所有的参数
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");

        Map<String, Object> filteredMap = new TreeMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String fieldName = declaredField.getName();
            if (map.containsKey(fieldName)) filteredMap.put(fieldName, map.get(fieldName));
        }

        return new PropertyValues(filteredMap);
    }

    /**
     * 添加绑定值: 把 HttpServletRequest 里的参数添加到绑定参数 PropertyValues 中
     */
    protected void addBindValues(PropertyValues pvs, HttpServletRequest request) {
    }

    /**
     * 将方法参数对象 target 与参数值 PropertyValues 进行绑定
     */
    private void doBind(PropertyValues pvs) {
        applyPropertyValues(pvs);
    }

    /**
     * 实际将参数值与对象属性进行绑定的方法
     */
    protected void applyPropertyValues(PropertyValues pvs) {
        getPropertyAccessor().setPropertyValues(pvs);
    }

    /**
     * 设置属性值的工具
     */
    protected AbstractPropertyAccessor getPropertyAccessor() {
        return propertyAccessor;
    }

    /**
     * 注册自定义编辑器
     */
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }
}
