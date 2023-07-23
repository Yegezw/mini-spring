package com.minis.web.servlet.adapter.convert.editor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 属性编辑器注册表支持
 */
public class PropertyEditorRegistrySupport {

    /**
     * 默认编辑器
     */
    private Map<Class<?>, PropertyEditor> defaultEditors;
    /**
     * 自定义默认编辑器
     */
    private Map<Class<?>, PropertyEditor> customEditors;

    public PropertyEditorRegistrySupport() {
        registerDefaultEditors();
    }

    /**
     * 注册默认的编辑器 editor(不同数据类型的参数编辑器)
     */
    protected void registerDefaultEditors() {
        createDefaultEditors();
    }

    /**
     * 创建默认的编辑器 editor, 对每一种数据类型规定一个默认的编辑器
     */
    private void createDefaultEditors() {
        this.defaultEditors = new HashMap<>(64);

        // int、Integer
        this.defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));
        this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
        // long、Long
        this.defaultEditors.put(long.class, new CustomNumberEditor(Long.class, false));
        this.defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, true));
        // float、Float
        this.defaultEditors.put(float.class, new CustomNumberEditor(Float.class, false));
        this.defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
        // double、Double
        this.defaultEditors.put(double.class, new CustomNumberEditor(Double.class, false));
        this.defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
        // BigDecimal、BigInteger
        this.defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
        this.defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));

        this.defaultEditors.put(String.class, new StringEditor(String.class, true));
    }

    /**
     * 获取默认的编辑器 editor
     */
    public PropertyEditor getDefaultEditor(Class<?> requiredType) {
        return defaultEditors.get(requiredType);
    }

    /**
     * 注册自定义编辑器
     */
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        if (customEditors == null) customEditors = new LinkedHashMap<>(16);
        customEditors.put(requiredType, propertyEditor);
    }

    /**
     * 查找自定义编辑器
     */
    public PropertyEditor findCustomEditor(Class<?> requiredType) {
        return getCustomEditor(requiredType);
    }

    /**
     * 获取自定义编辑器
     */
    public PropertyEditor getCustomEditor(Class<?> requiredType) {
        if (requiredType == null || customEditors == null) return null;
        // Check directly registered editor for type
        return customEditors.get(requiredType);
    }

    /**
     * 是否包含特定属性 elementType 的自定义编辑器
     */
    public boolean hasCustomEditorForElement(Class<?> elementType) {
        return (elementType != null && customEditors != null && customEditors.containsKey(elementType));
    }
}
